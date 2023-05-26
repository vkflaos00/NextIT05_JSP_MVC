<%@page import="kr.or.nextit.exception.DaoException"%>
<%@page import="kr.or.nextit.exception.BizException"%>
<%@page import="kr.or.nextit.member.service.MemberServiceImpl"%>
<%@page import="kr.or.nextit.member.service.IMemberService"%>
<%@page import="java.sql.SQLException"%>
<%@page import="kr.or.nextit.exception.BizNotEffectedException"%>
<%@page import="kr.or.nextit.exception.BizDuplicateKeyException"%>
<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.PreparedStatement"%>
<%@page import="java.sql.Connection"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@	taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath }/images/nextit_log.jpg" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/member.css">
</head>
<body>
<%-- <%request.setCharacterEncoding("utf-8"); %> --%>

<%-- 
<jsp:useBean id="member" class="kr.or.nextit.member.vo.MemberVO"></jsp:useBean>
<jsp:setProperty property="*" name="member"/>

<%
System.out.println("member.toString() :" + member.toString());

IMemberService memberService = new MemberServiceImpl();
try{
	memberService.registerMember(member);
}catch(BizDuplicateKeyException bde){
	bde.printStackTrace();
	request.setAttribute("bde", bde);
}catch(BizNotEffectedException bne){
	bne.printStackTrace();
	request.setAttribute("bne", bne);
}catch(DaoException de){
	de.printStackTrace();
	request.setAttribute("de", de);
}

%>
  --%>


<div class="container">
	<c:if test="${bne eq null and bde eq null and de eq null }">
		<h3>회원등록 성공</h3>
		<div class="alert alert-success">
			<p>정상적으로 회원 등록 되었습니다. 확인 클릭하시면 로그인 페이지로 이동합니다.</p>
			<div class="btn-area">
				<button type="button" 
					onclick="location.href='${pageContext.request.contextPath}/login/login.do'">확인</button>
			</div>
		</div>
	</c:if>
	<c:if test="${bde ne null }">
		<h3>회원등록 실패</h3>
		<div class="alert alert-warning">
			<p>회원등록에 실패하였습니다. 이미 사용중인 아이디입니다. 다른 아이디를 사용해주세요</p>
			<div class="btn-area">
				<button type="button" onclick="history.back();">뒤로가기</button> 
			</div>
		</div>	
	</c:if>
	
	<c:if test="${bne ne null or de ne null }">
		<h3>회원등록 실패</h3>
		<div class="alert alert-warning">
			<p>회원등록에 실패하였습니다. 전산실에 문의 부탁드립니다. 042-719-8850</p>
			<div class="btn-area">
				<button type="button" onclick="history.back();">뒤로가기</button> 
			</div>
		</div>	
	</c:if>
</div>
</body>
</html>