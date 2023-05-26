package kr.or.nextit.free.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.or.nextit.exception.BizNotEffectedException;
import kr.or.nextit.exception.BizNotFoundException;
import kr.or.nextit.exception.BizPasswordNotMatchedException;
import kr.or.nextit.exception.DaoException;
import kr.or.nextit.free.service.FreeBoardServiceImpl;
import kr.or.nextit.free.service.IFreeBoardService;
import kr.or.nextit.free.vo.FreeBoardVO;
import kr.or.nextit.member.vo.MemberVO;
import kr.or.nextit.servlet.NextITProcess;

public class FreeModify implements NextITProcess {

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {

		System.out.println("FreeModify process");

		request.setCharacterEncoding("UTF-8");

		MemberVO memberVO = (MemberVO) request.getSession().getAttribute("memberVO");
		if (memberVO == null) {
			return "redirect:/login/login.do?msg=none";
		}

		FreeBoardVO freeBoard = new FreeBoardVO();

		IFreeBoardService freeBoardService = new FreeBoardServiceImpl();
		try {
			freeBoardService.modifyBoard(freeBoard);
		} catch (BizNotFoundException bnf) {
			request.setAttribute("bnf", bnf);
			bnf.printStackTrace();
		} catch (BizPasswordNotMatchedException bpn) {
			request.setAttribute("bpn", bpn);
			bpn.printStackTrace();
		} catch (BizNotEffectedException bne) {
			request.setAttribute("bne", bne);
			bne.printStackTrace();
		} catch (DaoException de) {
			request.setAttribute("de", de);
			de.printStackTrace();
		}

		return "/WEB-INF/views/free/freeModify.jsp";
	}

}