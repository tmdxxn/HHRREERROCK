import React, { useEffect, useState } from 'react';
import {Link, useNavigate} from 'react-router-dom';

import '../css/Header.css'

function Header() {


    const navigate = useNavigate();
    const [accessToken, setAccessToken] = useState(localStorage.getItem('accessToken'));

    const handleLogout = async () => {
        try {

                localStorage.removeItem('accessToken');
                setAccessToken(null);
                alert('성공적으로 로그아웃되었습니다.');
                navigate('/')

        }
        catch (error) {
            console.error('handleLogout error:', error);
        }

    }

    useEffect(() => {
        setAccessToken(localStorage.getItem('accessToken'));
    }, []);


    return (
        <>

            <header className='CustomHeader'>
                {/* <!-- 헤더 오른쪽 --> */}
                <div className="header_right">
                    <div className="logo_img">
                        {/* <!-- 로고 이미지 --> */}
                        <Link to="/" className='white'><img src="http://placehold.it/70X70" alt="" /></Link>
                    </div>
                </div>
                {/* <!-- 검색창 --> */}
                <div className="search">
                    <form action="">
                        <input type="text" name="" id="" className="search_input" placeholder="오늘은 무슨 영화를 볼까?" />
                        <button className="HeadSearchBtn" type="submit">
                            <i className="fa-solid fa-magnifying-glass-arrow-right fa-3x" ></i>
                        </button>
                    </form>
                </div>
                {/* <!-- 헤더 왼쪽 --> */}
                <div className="header_left">

                    <div className="profile">
                        {/* <!-- 동그라미 프로필 공간 --> */}
                        <Link to="/admin/Notice" className='white'> 임시 <br />관리자 페이지</Link>
                    </div>
                    <div className="profile_menu">
                        <ul>
                            <Link to="/user/MyPage" className='white'><li>마이페이지</li></Link>
                            <Link to="/user/Notice" className='white'><li>공지사항</li></Link>
                            <li>
                                {
                                    accessToken ? (
                                            <div onClick={handleLogout} className='logout'>로그아웃</div>
                                        ) :
                                        (
                                            <Link to={"/login"}>로그인</Link>
                                        )
                                }
                            </li>
                        </ul>
                    </div>
                    {/* <!-- 추후에 로그인후에 로그아웃으로 바뀌도록 작성 --> */}
                </div>
            </header>





        </>

    );


}


export default Header;