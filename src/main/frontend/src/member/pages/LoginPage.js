import React, { useEffect } from 'react';
import {Link, useNavigate} from 'react-router-dom';
import google_login from '../../common/img/google_login.png';

// css
import '../../common/css/Login.css';

function LoginPage() {
    const navigate = useNavigate();

    useEffect(() => {
        const form = document.getElementById('login-form');

        const handleSubmit = async (e) => {
            e.preventDefault();

            const username = document.getElementById('username').value;
            const password = document.getElementById('password').value;

            const response = await fetch('/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    memId: username,
                    memPassword: password
                })
            });

            if (response.ok) {
                const data = await response.json();
                localStorage.setItem('accessToken', data.accessToken);
                localStorage.setItem('loginMethod', 'custom');
                navigate('/')
            } else {
                alert('로그인 실패');
            }
        };

        form.addEventListener('submit', handleSubmit);

        return () => {
            form.removeEventListener('submit', handleSubmit);
        };
    }, []);

    const handleGoogleLogin = () => {
        window.location.href = 'http://localhost:8080/oauth2/authorization/google';
    };

    return (
        <>
            <div className="login-container">
                <div id="login-header">
                    <h2>로그인</h2>
                </div>

                <form id="login-form">
                    <div className='wrap'>
                        <div className="box1">
                            <div className="box2">
                                <label htmlFor="username">아이디:</label>
                            </div>
                            <div className="box3">
                                <input type="text" id="username" name="username" required/>
                            </div>
                        </div>

                        <div className="box">
                            <div className="box2">
                                <label htmlFor="password">비밀번호:</label>
                            </div>
                            <div className="box3">
                                <input type="password" id="password" name="password" required/>
                            </div>
                        </div>
                    </div>

                    <div className="box4">
                        <input type="submit" className="submit" value="로그인"/>
                    </div>
                </form>

                <div className="google_login">
                    <button onClick={handleGoogleLogin}>
                        <img src={google_login} alt="구글로그인버튼" />
                    </button>
                </div>

                <div className="signup">
                    <div className="signupLink">
                        <Link to="/SignUp" className='white'>회원가입</Link>
                    </div>
                    <div className="signup3">
                        <Link to="/FindIdPassword" className='white'>아이디/비밀번호 찾기</Link>
                    </div>
                </div>
            </div>
        </>
    );
}

export default LoginPage;