package kr.or.nextit.member.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

import kr.or.nextit.exception.BizNotEffectedException;
import kr.or.nextit.exception.BizNotFoundException;
import kr.or.nextit.exception.BizPasswordNotMatchedException;
import kr.or.nextit.exception.DaoException;
import kr.or.nextit.member.service.IMemberService;
import kr.or.nextit.member.service.MemberServiceImpl;
import kr.or.nextit.member.vo.MemberVO;
import kr.or.nextit.servlet.NextITProcess;

public class MemberModify implements NextITProcess{

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		System.out.println("MemberModify process");
		 
		request.setCharacterEncoding("utf-8");
		
		MemberVO memberVO =  (MemberVO)request.getSession().getAttribute("memberVO");
		if(memberVO == null){
			return "redirect:/login/login.do?msg=none";
		}
		
		
		MemberVO member = new MemberVO();
		BeanUtils.populate(member, request.getParameterMap());
		
		System.out.println("member.toString():"+member.toString());
	
		IMemberService memberService = new MemberServiceImpl();
		try{
			memberService.modifyMember(member);
		}catch(BizNotEffectedException bne){
			request.setAttribute("bne", bne);
			bne.printStackTrace();
		}catch(BizPasswordNotMatchedException bpn){
			request.setAttribute("bpn", bpn);
			bpn.printStackTrace();
		}catch(BizNotFoundException bnf){
			request.setAttribute("bnf", bnf);
			bnf.printStackTrace();
		}catch(DaoException de){
			request.setAttribute("de", de);
			de.printStackTrace();
		}
 
		return "/WEB-INF/views/member/memberModify.jsp";
	}

}
