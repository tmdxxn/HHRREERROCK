import React, { useState, useEffect } from 'react';
import { useNavigate, useLocation } from "react-router-dom";
import axios from "axios";

// css
import "../../common/css/ChangePassword.css"

function ChangePassword() {
    const navigate = useNavigate();
    const location = useLocation();
    const [isResetMode, setIsResetMode] = useState(false);
    const [passwords, setPasswords] = useState({
        memNewPassword: '',
        memNewPasswordCheck: ''
    });
    const [passwordError, setPasswordError] = useState('');

    useEffect(() => {
        const params = new URLSearchParams(location.search);
        const tokenParam = params.get('token');
        if (tokenParam) {
            setIsResetMode(true);
            sessionStorage.setItem('resetToken', tokenParam);
            navigate(location.pathname, { replace: true });
        }
    }, [location, navigate]);

    const validatePassword = (password) => {
        const minLength = 8;
        const hasUpperCase = /[A-Z]/.test(password);
        const hasLowerCase = /[a-z]/.test(password);
        const hasNumbers = /\d/.test(password);
        const hasNonalphas = /\W/.test(password);

        if (password.length < minLength) {
            return '비밀번호는 최소 8자 이상이어야 합니다.';
        } else if (!(hasUpperCase && hasLowerCase && hasNumbers && hasNonalphas)) {
            return '비밀번호는 대문자, 소문자, 숫자, 특수문자를 모두 포함해야 합니다.';
        }
        return '';
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setPasswords(prevState => ({
            ...prevState,
            [name]: value
        }));
        if (name === 'memNewPassword') {
            setPasswordError(validatePassword(value));
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (passwordError) {
            alert(passwordError);
            return;
        }

        if (passwords.memNewPassword !== passwords.memNewPasswordCheck) {
            alert('새 비밀번호와 확인 비밀번호가 일치하지 않습니다.');
            return;
        }

        try {
            let response;
            const updatePasswordDto = {
                memNewPassword: passwords.memNewPassword,
                memNewPasswordCheck: passwords.memNewPasswordCheck
            };

            if (isResetMode) {
                const token = sessionStorage.getItem('resetToken');
                response = await axios.post('/auth/reset-password',
                    updatePasswordDto,
                    { params: { token } }
                );
                sessionStorage.removeItem('resetToken');
            } else {
                const accessToken = localStorage.getItem('accessToken');
                response = await axios.put('/auth/update', updatePasswordDto, {
                    headers: { 'Authorization': `Bearer ${accessToken}` }
                });
            }

            if (response.status === 200) {
                alert('비밀번호가 성공적으로 변경되었습니다.');
                navigate(isResetMode ? '/login' : -1);
            }
        } catch (error) {
            console.error('Error changing password:', error);
            alert(error.response?.data || '비밀번호를 재설정하는 동안 오류가 발생했습니다');
        }
    };

    return (
        <>
            <div className="cp_title">
                <h2>{isResetMode ? '비밀번호 재설정' : '비밀번호 변경'}</h2>
            </div>

            <div className="password_form_div">
                <form className='ChangePasswordForm' onSubmit={handleSubmit}>
                    <div className="change_password">
                        <div>
                            {isResetMode ? '새 비밀번호:' : '비밀번호 변경:'}
                        </div>
                        <div>
                            <input
                                type="password"
                                name="memNewPassword"
                                id=""
                                className="new_password"
                                value={passwords.memNewPassword}
                                onChange={handleChange}
                                required
                            />
                        </div>
                    </div>

                    {passwordError && <div className="error-message">{passwordError}</div>}

                    <div className="change_password">
                        <div>
                            비밀번호 재확인:
                        </div>
                        <div>
                            <input
                                type="password"
                                name="memNewPasswordCheck"
                                id=""
                                className="confirm_password"
                                value={passwords.memNewPasswordCheck}
                                onChange={handleChange}
                                required
                            />
                        </div>
                    </div>

                    <div className="submit_button">
                        <input type="submit"
                               name=""
                               id=""
                               className="ChangePwBtn"
                               value={isResetMode ? '비밀번호 재설정' : '변경하기'} />
                    </div>
                </form>
            </div>

            {!isResetMode && (
                <div className="return_button">
                    <button className="ReturnBtn"
                            onClick={() => navigate(-1)}>돌아가기
                    </button>
                </div>
            )}
        </>
    );
}

export default ChangePassword;