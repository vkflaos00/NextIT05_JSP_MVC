<%@page import="java.util.List"%>
<%@page import="kr.or.nextit.code.vo.CodeVO"%>
<%@page import="kr.or.nextit.code.service.CommCodeServiceImpl"%>
<%@page import="kr.or.nextit.code.service.ICommCodeService"%>
<%@page import="kr.or.nextit.exception.DaoException"%>
<%@page import="kr.or.nextit.exception.BizNotEffectedException"%>
<%@page import="kr.or.nextit.free.vo.FreeBoardVO"%>
<%@page import="kr.or.nextit.free.service.FreeBoardServiceImpl"%>
<%@page import="kr.or.nextit.free.service.IFreeBoardService"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
	request.setCharacterEncoding("UTF-8");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>NextIT</title>
<link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath }/images/nextit_log.jpg" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/header.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/freeBoardEdit.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath }/css/footer.css">
<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<script>
function fn_submitCheck(){
	console.log("fn_submitCheck");
	
	if($("input[name='boTitle']").val() == ""){
		alert("제목을 작성해주세요");
		return;
	}else if($("input[name='boPass']").val() == ""){
		alert("비밀번호를 입력해주세요");
		return;
	}else if($("input[name='boCategory']").val()==""){
		alert("글 분류를 선택해주세요");
		return;
	}
	
	let ret = confirm("저장하시겠습니까?");
	if(ret){
		let f= document.freeModify;
		f.submit();
	}else{
		alert("취소하셧습니다.");
	}
	
}
</script>
</head>
<body>
<%
	//코드 계층화 로직 추가하기
	ICommCodeService codeService = new CommCodeServiceImpl();
	List<CodeVO> categoryList = codeService.getCodeListByParent("BC00");
	
	request.setAttribute("categoryList", categoryList);
%>


<%
	String boNo = request.getParameter("boNo");
	System.out.println("boNo : " + boNo);
	IFreeBoardService freeBoardService = new FreeBoardServiceImpl();
	
	try{
		FreeBoardVO freeBoard = freeBoardService.getBoard(boNo);
		System.out.println("freeBoard: "+ freeBoard.toString());
		request.setAttribute("freeBoard", freeBoard);
	}catch(BizNotEffectedException bne){
		request.setAttribute("bne", bne);
		bne.printStackTrace();
	}catch(DaoException de){
		request.setAttribute("de", de);
		de.printStackTrace();
	}
%>
<div id="wrap">
    <div class="header">
        <div class="top_nav">
            <!-- header 영역 -->
            <%@ include file="/WEB-INF/views/header/header.jsp" %>
        </div>
    </div>
    <!-- header e -->

    <div class="intro_bg">
        <div class="intro_text">
            <h1>NextIT</h1>
            <h4>넥스트아이티</h4>
        </div>
    </div>
    <!-- intro_bg e -->

    <!-- 전체 영역잡기 -->
    <div class="contents">
        <!-- 사용할 영역잡기 -->
        <div class="content01">
            <div class="content01_h1">
                <h1>자유게시판</h1>
            </div>
            
			<!-- 해당 정보를 불러오지 못한 경우 처리 -->
				<c:if test="${bne ne null or de ne null }">
					<div class="alert alert-warning">
							해당글을 불러오지 못하였습니다. 전산실에 문의 부탁드립니다. 042-719-8850
					</div>	
					<div class="div_button">
	                      <input type="button" onclick="location.href='${pageContext.request.contextPath}/free/freeList.jsp'" value="목록">
	                </div>
                </c:if>
	                  
            
            <!-- 해당 정보를 올바르게 불러온 경우 처리  -->
            	<c:if test="${bne eq null and de eq null }">
            		<form name="freeModify" action="${pageContext.request.contextPath}/free/freeModify.jsp" method="post">
            	      <div id="div_table">
	                      <table>
	                          <colgroup>
	                              <col width="200">
	                              <col width="400">
	                          </colgroup>
	                          <tr>
	                              <td class="td_left">글번호</td>
	                              <td class="td_right">
	                                  ${freeBoard.boNo }
	                                  <input type="hidden" name="boNo" value="${freeBoard.boNo }">
	                              </td>
	                          </tr>
	                          <tr>
	                              <td class="td_left">글제목</td>
	                              <td class="td_right">
	                              		<input type="text" name="boTitle" value="${freeBoard.boTitle }" required="required">
	                              </td>
	                          </tr>
	                          <tr>
	                              <td class="td_left">작성자명</td>
	                              <td class="td_right">
										${freeBoard.boWriter } 
										<input type="hidden" name="boWriter" value="${freeBoard.boWriter }">
	                              </td>
	                          </tr>
	                          <tr>
	                              <td class="td_left">글 비번</td>
	                              <td class="td_right">
										<input type="password" name="boPass" value="" autocomplete="off"  required="required" pattern="^\w{4,20}$" title="알파벳과 숫자로 최소4글자 최대20글자 이내 입력"  >
	                              </td>
	                          </tr>
	                          <tr>
	                              <td class="td_left">글 분류</td>
	                              <td class="td_right">
	                              		<select name="boCategory"  required="required">
	                              			
	                              			<%-- c:foreach로 변경 
			                              		<option value="">-- 선택하세요--</option>
			                              		<option value="BC01"  ${freeBoard.boCategory eq "BC01" ? "selected='selected'"  : ""} >프로그램</option>
												<option value="BC02" ${freeBoard.boCategory eq "BC02" ? "selected='selected'"  : ""}>웹</option>
												<option value="BC03" ${freeBoard.boCategory eq "BC03" ? "selected='selected'"  : ""}>사는 이야기</option>
												<option value="BC04" ${freeBoard.boCategory eq "BC04" ? "selected='selected'"  : ""}>취업</option>
											 --%>
								
											 <option value="">-- 선택하세요--</option>
											 <c:forEach items="${categoryList }" var="category">
										          <option value="${category.commCd}"  ${freeBoard.boCategory eq category.commCd ? "selected='selected'"  : ""}     >${category.commNm}</option>
											  </c:forEach>

	                              		</select>
	                              </td>
	                          </tr>
	                          <tr>
	                              <td class="td_left">내용</td>
	                              <td class="td_right">
	                              		<textarea rows="10" name="boContent" >${freeBoard.boContent }</textarea>
	                              </td>
	                          </tr>
	                          <tr>
	                              <td class="td_left">IP</td>
	                              <td class="td_right">
	                              		<%-- 
                              			<%=request.getRemoteAddr()%>
	                              		<input type="text" name="boIp" value="<%=request.getRemoteAddr()%>">  
	                              		--%>
	                              		${pageContext.request.remoteAddr}
	                              		<input type="hidden" name="boIp" value="${pageContext.request.remoteAddr}">
	                              </td>
	                          </tr>
	                          <tr>
	                              <td class="td_left">조회수</td>
	                              <td class="td_right">
	                              		${freeBoard.boHit }
	                              </td>
	                          </tr>
	                      </table>
	                  </div>
                  
		                <!-- 버튼 -->
		                <div class="div_button">
		                	<c:if test="${freeBoard.boWriter eq memberVO.memId  }">
			                    <input type="button" onclick="fn_submitCheck()" value="저장">
		                	</c:if>
		                    <input type="button" onclick="location.href='${pageContext.request.contextPath}/free/freeList.jsp'" value="목록">
		                </div>
                  	</form>
             	</c:if>
             
        </div>
    </div>

	<!-- footer -->
	<footer id="page_footer">
		<!-- footer영역 -->
		<%@ include file="/WEB-INF/views/footer/footer.jsp" %>
    </footer>
</div>  
</body>
</html>