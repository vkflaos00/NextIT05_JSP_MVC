package kr.or.nextit.member.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.or.nextit.common.vo.RoleInfoVO;
import kr.or.nextit.common.vo.UserRoleVO;
import kr.or.nextit.exception.BizDuplicateKeyException;
import kr.or.nextit.exception.BizNotEffectedException;
import kr.or.nextit.exception.BizNotFoundException;
import kr.or.nextit.exception.BizPasswordNotMatchedException;
import kr.or.nextit.member.dao.IMemberDao;
import kr.or.nextit.member.dao.MemberDaoImpl;
import kr.or.nextit.member.vo.MemberSearchVO;
import kr.or.nextit.member.vo.MemberVO;

public class MemberServiceImpl implements IMemberService {

	IMemberDao memberDao = new MemberDaoImpl();
	

	@Override
	public void registerMember(MemberVO member) throws BizDuplicateKeyException, BizNotEffectedException {
		// TODO Auto-generated method stub
		System.out.println("void registerMember");
		
		if(member.getMemId() != null && !member.getMemId().equals("")) {
			MemberVO  vo = memberDao.getMember(member.getMemId());
			if( vo != null ) {
				throw new BizDuplicateKeyException();
			}
			int resultCnt = memberDao.insertMember(member);
			if (resultCnt != 1) {
				throw new BizNotEffectedException();
			} 
			int resultCnt2 = memberDao.insertUserRole(member);
			if (resultCnt2 != 1) {
				throw new BizNotEffectedException();
			}
		}
	}

	
	@Override
	public boolean loginCheck(MemberVO member, HttpServletRequest request, HttpServletResponse response) throws BizNotEffectedException {
		// TODO Auto-generated method stub

		MemberVO vo = null;
		if(member.getMemId()==null || member.getMemId().equals("")) {
			/*try {
				response.sendRedirect(request.getContextPath()+"/login/login.jsp?msg=null");
				return;
				
			} catch (IOException e) {
				e.printStackTrace();
			}*/
			return false;
		}else {
			vo = memberDao.loginCheck(member);
		}
		
		try {
			if(vo == null){
				System.out.println("do not get member info ");
				//response.sendRedirect(request.getContextPath()+"/login/login.jsp?msg=fail");
				return false;
			}else{
				System.out.println("success login");
				
				List<UserRoleVO> userRoleList = memberDao.getUserRole(member);
				if(userRoleList != null) {
					vo.setUserRoleList(userRoleList);
				}
				
				HttpSession session = request.getSession();
				session.setAttribute("memberVO", vo);
					
				String rememberMe = member.getRememberMe();
				if (rememberMe != null && rememberMe.equals("Y")) {
					System.out.println("rememberMe is Y");
					Cookie cookie= new Cookie("rememberMe", member.getMemId());
					cookie.setMaxAge(60*60*24); 
					cookie.setHttpOnly(true);
					//cookie.setSecure(true);
					response.addCookie(cookie);
				}else{
					Cookie cookie= new Cookie("rememberMe", "");
					cookie.setMaxAge(0);
					response.addCookie(cookie);
				}
				//response.sendRedirect(request.getContextPath()+"/home/home.jsp");
				return true;
			}
		} catch (Exception e) {
			throw new BizNotEffectedException();
		}
	}


	@Override
	public MemberVO getMember(String memId) throws BizNotEffectedException {
		// TODO Auto-generated method stub
		
		MemberVO member = null;
		if(memId != null && ! memId.equals("")) {
			member = memberDao.getMember(memId);
		}
		if(member == null) {
			throw new BizNotEffectedException();
		}
		return member;
	}

	@Override	
	public void modifyMember(MemberVO member) throws BizNotFoundException, BizPasswordNotMatchedException, BizNotEffectedException {
		// TODO Auto-generated method stub
		
		MemberVO vo = null;
		if(member.getMemId() != null && ! member.getMemId().equals("")) {
			vo = memberDao.getMember(member.getMemId());
		}
		
		if(vo == null) {
			throw new BizNotFoundException();
		}
		if( !vo.getMemPass().equals(member.getMemPass())) {
			throw new BizPasswordNotMatchedException();
		}
		
		int resultCnt = memberDao.updateMember(member);
		
		if(resultCnt != 1){
			throw new BizNotEffectedException();
		}
	
	}

	@Override
	public void removeMember(MemberVO member) throws BizNotFoundException, BizPasswordNotMatchedException, BizNotEffectedException {
		// TODO Auto-generated method stub
		
		MemberVO vo = null;
		if(member.getMemId() != null && ! member.getMemId().equals("")) {
			vo = memberDao.getMember(member.getMemId());
		}
		
		if( vo == null) {
			throw new BizNotFoundException();
		}
		if( !vo.getMemPass().equals(member.getMemPass()) ){
			throw new BizPasswordNotMatchedException();
		}
		
		int resultCnt = memberDao.deleteMember(member);
		if(resultCnt != 1){
			throw new BizNotEffectedException();
		}
	}

	@Override
	public List<MemberVO> getMemberList(MemberSearchVO searchVO) throws BizNotFoundException {
		// TODO Auto-generated method stub

		int totalRowCount  = memberDao.getTotalRowCount(searchVO);
		searchVO.setTotalRowCount(totalRowCount);
		searchVO.pageSetting();
		System.out.println("searchVO.toString() : "+searchVO.toString());

		List<MemberVO> memberVO = memberDao.getMemberList(searchVO);
		
		if(memberVO == null) {
			throw new BizNotFoundException();
		}
		return memberVO;
	}


	@Override
	public void removeMultiMember(String memMultiId) throws BizNotEffectedException {
		// TODO Auto-generated method stub
		
		System.out.println("memMultiId: "+ memMultiId);
		
		ObjectMapper objectMapper = new ObjectMapper();
		
		
		try { 
				List<Object> list  = objectMapper.readValue(memMultiId, new TypeReference<List<Object>>(){});
				System.out.println("list: "+ list);
				
				if(list.size() == 0) {
					throw new BizNotEffectedException();
				}
				
				for(int i=0; i<list.size(); i++) {
					String memId = (String) list.get(i);
					MemberVO member = new MemberVO();
					member.setMemId(memId);
					int resultCnt = memberDao.deleteMember(member);
					if(resultCnt ==0) {
						throw new BizNotEffectedException();	
					}
				}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BizNotEffectedException();
		}
	}


	@Override
	public MemberVO getMemberRole(String memId) throws BizNotEffectedException {
		// TODO Auto-generated method stub
		System.out.println("MemberServiceImpl memId: "+ memId);
		
		MemberVO member = null;
		if(memId != null && ! memId.equals("")) {
			member =  memberDao.getMember(memId);
		}
		if(member == null) {
			throw new BizNotEffectedException();
		}
		
		List<UserRoleVO>  userRoleList =memberDao.getUserRole(member);
		
		if(userRoleList == null) {
			throw new BizNotEffectedException();
		}
		
		member.setUserRoleList(userRoleList);
		
		return member;
	}


	@Override
	public List<RoleInfoVO> getRoleInfo() throws BizNotEffectedException {
		// TODO Auto-generated method stub
		
		List<RoleInfoVO> roleInfoList = memberDao.getRoleInfo();
		
		if(roleInfoList ==null) {
			throw new BizNotEffectedException();
		}
		return roleInfoList;
	}

	@Override
	public void updateUserRole(String memId, String[] roles) throws BizNotEffectedException {
		// TODO Auto-generated method stub

		System.out.println("roles.length :"+ roles.length);
		
		if( memId != null && ! memId.equals("")) {
			memberDao.deleteUserRole(memId);
			
			if(roles.length >0 ) {
				for(int i=0; i<roles.length; i++) {
					memberDao.insertMultiRole(memId, roles[i]);
				}
			}
		}else {
			throw new BizNotEffectedException();
		}
	}
 

}