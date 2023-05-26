package kr.or.nextit.member.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;

import kr.or.nextit.code.service.CommCodeServiceImpl;
import kr.or.nextit.code.service.ICommCodeService;
import kr.or.nextit.code.vo.CodeVO;
import kr.or.nextit.exception.BizNotFoundException;
import kr.or.nextit.exception.DaoException;
import kr.or.nextit.member.service.IMemberService;
import kr.or.nextit.member.service.MemberServiceImpl;
import kr.or.nextit.member.vo.MemberSearchVO;
import kr.or.nextit.member.vo.MemberVO;
import kr.or.nextit.servlet.NextITProcess;

public class MemberList implements NextITProcess {

	@Override
	public String process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		
		System.out.println("MemberList process");
		
//		<jsp:useBean id="searchVO" class="kr.or.nextit.member.vo.MemberSearchVO"></jsp:useBean>
//		<jsp:setProperty property="*" name="searchVO"/>

			MemberSearchVO searchVO = new MemberSearchVO();
			BeanUtils.populate(searchVO, request.getParameterMap());
		
			request.setCharacterEncoding("UTF-8");

			ICommCodeService codeService = new CommCodeServiceImpl();	
			List<CodeVO> jobList  = codeService.getCodeListByParent("JB00");
			request.setAttribute("jobList", jobList);
			
			List<CodeVO> hobbyList  = codeService.getCodeListByParent("HB00");
			request.setAttribute("hobbyList", hobbyList);


			IMemberService memberService = new MemberServiceImpl();
			try{
				List<MemberVO> memberList = memberService.getMemberList(searchVO);
				request.setAttribute("memberList", memberList);
				request.setAttribute("searchVO", searchVO);
			}catch(BizNotFoundException bnf){
				request.setAttribute("bnf", bnf);
				bnf.printStackTrace();
			}catch(DaoException de){
				request.setAttribute("de", de);
				de.printStackTrace();
			}

		
		return "/WEB-INF/views/member/memberList.jsp";
	}
	
}
