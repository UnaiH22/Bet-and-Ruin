package createUserTest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import businessLogic.BlFacade;
import businessLogic.BlFacadeImplementation;
import dataAccess.DataAccess;
import domain.User;
import exceptions.AlreadyTakenEMailException;
import exceptions.NotValidEmailException;
import exceptions.NullParameterException;
import exceptions.UserAlreadyTakenException;

class RegisterUserBLMockTest {
	DataAccess dataAccess = Mockito.mock(DataAccess.class);
	BlFacade sut = new BlFacadeImplementation(dataAccess);
	
	@Test
	@DisplayName("User correctly added")
	void test1() {
		String usrname="Unai";
		String passw="123456789ABC";
		String eMail="unasiheras@ehu.eus";
		User usr=new User(usrname, passw, eMail);
		
		try {
			Mockito.when(dataAccess.createUser(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenReturn(usr);
			assertTrue(sut.registerUser(usrname, passw, eMail));
		} catch (Exception e) {
			fail("Something went wrong");	
		}

	}
			
	@Test
	@DisplayName("user already in DB")
	void test2() {
		String usrname="Unai";
		String passw="123456789ABC";
		String eMail="unasiheras@ehu.eus";
		//User usr=null;
		try {	
			Mockito.when(dataAccess.createUser(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenThrow(new UserAlreadyTakenException());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		assertThrows(UserAlreadyTakenException.class, () -> sut.registerUser(usrname, passw, eMail));

	}
	
	@Test
	@DisplayName("Empty user")
	void test3() {
		String usrname="";
		String passw="123456789ABC";
		String eMail="unasiheras@ehu.eus";
		
		try {
			Mockito.when(dataAccess.createUser(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenThrow(new NullParameterException());
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertThrows(NullParameterException.class, () -> sut.registerUser(usrname, passw, eMail));
	}
	
	@Test
	@DisplayName("Empty password")
	void test4() {
		String usrname="Unai";
		String passw="";
		String eMail="unasiheras@ehu.eus";
		
		try {
			Mockito.when(dataAccess.createUser(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenThrow(new NullParameterException());
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertThrows(NullParameterException.class, () -> sut.registerUser(usrname, passw, eMail));	}
	
	@Test
	@DisplayName("Empty eMail")
	void test5() {
		String usrname="Unai";
		String passw="123456789ABC";
		String eMail="";
	
		try {
			Mockito.when(dataAccess.createUser(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenThrow(new NullParameterException());
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertThrows(NullParameterException.class, () -> sut.registerUser(usrname, passw, eMail));	}
	
	@Test
	@DisplayName("eMail alredy in use")
	void test6() {
		String usrname="Unai";
		String passw="123456789ABC";
		String eMail="unasiheras@ehu.eus";
		
		try {
			Mockito.when(dataAccess.createUser(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenThrow(new AlreadyTakenEMailException());
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertThrows(AlreadyTakenEMailException.class, () -> sut.registerUser(usrname, passw, eMail));

	}
	
	@Test
	@DisplayName("not valid eMail")
	void test7() {
		String usrname="Unai";
		String passw="123456789ABC";
		String eMail="h0l4.1im";
		
		try {
			Mockito.when(dataAccess.createUser(Mockito.anyString(), Mockito.anyString(), Mockito.anyString())).thenThrow(new NotValidEmailException());
		} catch (Exception e) {
			e.printStackTrace();
		}
		assertThrows(NotValidEmailException.class, () -> sut.registerUser(usrname, passw, eMail));

	}


}
