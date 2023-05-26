package kr.or.nextit.free.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

import kr.or.nextit.exception.BizNotEffectedException;
import kr.or.nextit.exception.DaoException;
import kr.or.nextit.free.service.FreeBoardServiceImpl;
import kr.or.nextit.free.service.IFreeBoardService;
import kr.or.nextit.free.vo.FreeBoardSearchVO;
import kr.or.nextit.free.vo.FreeBoardVO;
import kr.or.nextit.member.vo.MemberVO;
import kr.or.nextit.servlet.NextITProcess;

public class FreeView implements NextITProcess {

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {

		System.out.println("FreeView process");

		request.setCharacterEncoding("UTF-8");

		MemberVO memberVO = (MemberVO) request.getSession().getAttribute("memberVO");
		if (memberVO == null) {
			return "redirect:/login/login.do?msg=none";
		}

		String boNo = request.getParameter("boNo");
		IFreeBoardService freeBoardService = new FreeBoardServiceImpl();
		
		FreeBoardVO freeBoard = freeBoardService.getBoard(boNo);
		BeanUtils.populate(freeBoard, request.getParameterMap());
		
		try {

			freeBoardService.increaseHit(boNo);

			System.out.println("freeBoard: " + freeBoard.toString());
			request.setAttribute("freeBoard", freeBoard);
			request.setAttribute("boNo", boNo);
			
		} catch (BizNotEffectedException bne) {
			request.setAttribute("bne", bne);
			bne.printStackTrace();
		} catch (DaoException de) {
			request.setAttribute("de", de);
			de.printStackTrace();
		}

		return "/WEB-INF/views/free/freeView.jsp";
	}

}
