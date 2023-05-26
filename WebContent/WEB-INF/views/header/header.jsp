<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<div class="searchArea">
	<form action="">
		<input type="text" id="input_search" name="input_search"
		 value="" placeholder="Search">
		<span>검색</span>
	</form>
</div>
<ul class="nav">
	<li><a href="">HOME</a></li>
	<li><a href="#about">ABOUT</a></li>
	<li><a href="#service">SERVICE</a></li>
	<li><a href="#content">CONTENT</a></li>
	<li><a href="">FREEBOARD</a></li>
	<li><a href="#">${memberVO.memId }</a></li>
</ul>