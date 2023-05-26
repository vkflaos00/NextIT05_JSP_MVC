package kr.or.nextit.free.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

import kr.or.nextit.code.service.CommCodeServiceImpl;
import kr.or.nextit.code.service.ICommCodeService;
import kr.or.nextit.code.vo.CodeVO;
import kr.or.nextit.exception.BizNotEffectedException;
import kr.or.nextit.exception.DaoException;
import kr.or.nextit.free.service.FreeBoardServiceImpl;
import kr.or.nextit.free.service.IFreeBoardService;
import kr.or.nextit.free.vo.FreeBoardSearchVO;
import kr.or.nextit.free.vo.FreeBoardVO;
import kr.or.nextit.member.vo.MemberVO;
import kr.or.nextit.servlet.NextITProcess;

public class FreeList implements NextITProcess{

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		
		System.out.println("FreeList process");
		
		request.setCharacterEncoding("UTF-8");
		
		
		MemberVO memberVO =  (MemberVO)request.getSession().getAttribute("memberVO");
		if(memberVO == null){
			return "redirect:/login/login.do?msg=none";
		}
		
	
//		<%-- <jsp:useBean id="pagingVO" class="kr.or.nextit.common.vo.PagingVO"></jsp:useBean>
//		<jsp:setProperty property="*" name="pagingVO"/> --%>
//		<jsp:useBean id="searchVO" class="kr.or.nextit.free.vo.FreeBoardSearchVO"></jsp:useBean>
//		<jsp:setProperty property="*" name="searchVO"/>
	

		FreeBoardSearchVO searchVO = new FreeBoardSearchVO();
		BeanUtils.populate(searchVO, request.getParameterMap());
	
		ICommCodeService codeService = new CommCodeServiceImpl();
		List<CodeVO> categoryList = codeService.getCodeListByParent("BC00");
		request.setAttribute("categoryList", categoryList);
	
		IFreeBoardService freeBoardService = new FreeBoardServiceImpl();

		try{
			//List<FreeBoardVO> freeBoardList = freeBoardService.getBoardList(pagingVO);
			List<FreeBoardVO> freeBoardList = freeBoardService.getBoardList(searchVO);
			request.setAttribute("freeBoardList", freeBoardList);
		}catch(BizNotEffectedException bne){
			bne.printStackTrace();
			request.setAttribute("bne", bne);
		}catch(DaoException de){
			de.printStackTrace();
			request.setAttribute("de", de);
		}
			
		return "/WEB-INF/views/free/freeList.jsp";
	}
	
}
