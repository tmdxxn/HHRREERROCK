import React, { useState, useEffect } from "react";
import axios from "axios";
import '../../../common/css/AdminMemberList.css';

function AdminMemberListPage() {

    const [members, setMembers] = useState([]);
    
    const [currentPage, setCurrentPage] = useState(0);
    
    const [totalPages, setTotalPages] = useState(0);
    
    const [searchTerm, setSearchTerm] = useState("");
    
    const [selectedMembers, setSelectedMembers] = useState([]);

    useEffect(() => {
        fetchMembers();
    }, [currentPage]);

    // 회원 목록 가져오는 로직
    const fetchMembers = async () => {
        try {
            const response = await axios.get(`/admin/members?page=${currentPage}&size=15`);
            setMembers(response.data.content);
            setTotalPages(response.data.totalPages);
        } catch (error) {
            console.error("Error fetching members:", error);
        }
    };

    // 회원 검색 로직
    const handleSearch = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.get(`/admin/members/search?term=${searchTerm}`);
            setMembers(response.data);
        } catch (error) {
            console.error("Error searching members:", error);
        }
    };

    // 체크박스 상태 변경
    const handleCheckboxChange = (memNum) => {
        setSelectedMembers(prev =>
            prev.includes(memNum) ? prev.filter(id => id !== memNum) : [...prev, memNum]
        );
    };

    // 회원 탈퇴 로직
    const handleDeleteMembers = async () => {
        if (window.confirm("선택한 회원을 삭제하시겠습니까?")) {
            try {
                const membersToDelete = members
                    .filter(member => selectedMembers.includes(member.memNum))
                    .map(member => member.memId);

                const response = await axios.post("/admin/members/delete", membersToDelete);

                // 서버 응답 처리
                if (response.data.deletedMembers && response.data.failedToDeleteMembers) {
                    let message = "";
                    if (response.data.deletedMembers.length > 0) {
                        message += `다음 회원들이 삭제되었습니다: ${response.data.deletedMembers.join(", ")}\n`;
                    }
                    if (response.data.failedToDeleteMembers.length > 0) {
                        message += `다음 회원들은 삭제할 수 없습니다 (관리자 권한): ${response.data.failedToDeleteMembers.join(", ")}`;
                    }
                    alert(message);
                } else {
                    alert(response.data);
                }

                fetchMembers();
                setSelectedMembers([]);
            } catch (error) {
                console.error("회원 삭제 중 오류 발생:", error.response?.data || error.message);
                alert(`회원 삭제 중 오류가 발생했습니다: ${error.response?.data || error.message}`);
            }
        }
    };

    return (
        <>
            <div className="list_div">
                <div className="admin_member_haed">
                    <h2>회원 관리</h2>
                </div>
                <div className="admin_member_list">
                    <div>
                        <ul className="list content">
                            <li className="checkbox"></li>
                            <li className="mem_no">회원 번호</li>
                            <li className="mem_id">회원 ID</li>
                            <li className="mem_email">이메일</li>
                            <li className="mem_phone">연락처</li>
                            <li className="mem_gender">성별</li>
                            <li className="mem_birth">생년월일</li>
                        </ul>
                    </div>
                    <div>
                        {members.map((member) => (
                            <ul className="list" key={member.memNum}>
                                <li className="checkbox">
                                    <input
                                        type="checkbox"
                                        checked={selectedMembers.includes(member.memNum)}
                                        onChange={() => handleCheckboxChange(member.memNum)}
                                    />
                                </li>
                                <li className="mem_no">{member.memNum}</li>
                                <li className="mem_id">{member.memId}</li>
                                <li className="mem_email">{member.memEmail}</li>
                                <li className="mem_phone">{member.memTel}</li>
                                <li className="mem_gender">{member.memGender}</li>
                                <li className="mem_birth">{member.memBirth}</li>
                            </ul>
                        ))}
                    </div>
                </div>

                <div className="list_number">
                    <ul className="list_number_ul">
                        <li onClick={() => setCurrentPage(prev => Math.max(prev - 1, 0))}>&lt;</li>
                        {[...Array(totalPages).keys()].map((number) => (
                            <li key={number} onClick={() => setCurrentPage(number)}>{number + 1}</li>
                        ))}
                        <li onClick={() => setCurrentPage(prev => Math.min(prev + 1, totalPages - 1))}>&gt;</li>
                    </ul>
                </div>

                <div className="admmin_member_search_div">
                    <div className="member_search_form">
                        <form onSubmit={handleSearch}>
                            <input
                                type="text"
                                placeholder="회원 검색"
                                value={searchTerm}
                                onChange={(e) => setSearchTerm(e.target.value)}
                            />
                            <input type="submit" value="검색"/>
                        </form>
                    </div>
                    <div className="member_edit_btn">
                        <button className="delete_member" onClick={handleDeleteMembers}>회원 삭제</button>
                    </div>
                </div>
            </div>
        </>
    );
}

export default AdminMemberListPage;