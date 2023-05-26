package kr.or.nextit.free.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.or.nextit.exception.BizNotEffectedException;
import kr.or.nextit.exception.DaoException;
import kr.or.nextit.free.service.FreeBoardServiceImpl;
import kr.or.nextit.free.service.IFreeBoardService;
import kr.or.nextit.free.vo.FreeBoardVO;
import kr.or.nextit.member.vo.MemberVO;
import kr.or.nextit.servlet.NextITProcess;

public class FreeRegister implements NextITProcess {

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {

		System.out.println("FreeRegister process");

		request.setCharacterEncoding("UTF-8");

		MemberVO memberVO = (MemberVO) request.getSession().getAttribute("memberVO");
		if (memberVO == null) {
			return "redirect:/login/login.do?msg=none";
		}
		FreeBoardVO freeBoard = new FreeBoardVO();

		System.out.println("freeBoard.toString(): " + freeBoard.toString());

		IFreeBoardService freeBoardService = new FreeBoardServiceImpl();
		try {
			freeBoardService.registerBoard(freeBoard);
		} catch (BizNotEffectedException bne) {
			bne.printStackTrace();
			request.setAttribute("bne", bne);
		} catch (DaoException de) {
			de.printStackTrace();
			request.setAttribute("de", de);
		}

		return "/WEB-INF/views/free/freeRegister.jsp";
	}

}
