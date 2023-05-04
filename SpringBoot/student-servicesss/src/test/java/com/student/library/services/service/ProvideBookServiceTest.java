//package com.student.library.services.service;
//
//import java.time.Instant;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.when;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import com.student.library.services.client.BookServiceClient;
//import com.student.library.services.entity.Book;
//import com.student.library.services.entity.RequestBook;
//import com.student.library.services.entity.Student;
//import com.student.library.services.enums.RequestStatus;
//import com.student.library.services.enums.Status;
//import com.student.library.services.exception.BookNotProvidedException;
//import com.student.library.services.exception.Constants;
//import com.student.library.services.repository.BookRepository;
//import com.student.library.services.repository.RequestRepository;
//import com.student.library.services.request.ProvideBookRequest;
//
//@ExtendWith(MockitoExtension.class)
//public class ProvideBookServiceTest {
//	@InjectMocks
//	ProvideBookService provideBookService;
//	@Mock
//	BookRepository bookRepository;
//	@Mock
//	RequestRepository requestRepository;
//	@Mock
//	RequestBookService requestBookService;
//	@Mock
//	BookServiceClient bookServiceClient;
//	@Mock
//	AuthService authService;
//	@Mock
//	RequestServiceTest requestServiceTest;
//
//	ProvideBookRequest provideBookRequest = new ProvideBookRequest(1L, 1L, Instant.now(), 0, RequestStatus.APPROVED);
//
//	Student studentResponse = new Student(1L, "anjali", "anjali", "cse", 1, "12345", "anjali@gmail.com");
//
//	Book book = new Book(1L, "core", "martin", Status.AVAILABLE, "anjali", "1", 8, "computers");
//	RequestBook requestBooks = new RequestBook(1L, "core", RequestStatus.REJECTED, "12345", 1, Instant.now(),
//			studentResponse, book);
//	RequestBook requestBook = new RequestBook(1L, "core", RequestStatus.REQUESTED, "12345", 1, Instant.now(),
//			studentResponse, book);
//
//	@Test
//	public void testProvideBook() throws BookNotProvidedException {
//		List<RequestBook> books = new ArrayList<RequestBook>();
//		books.add(requestBook);
//		when(requestBookService.getRequestBook(requestBook.getId())).thenReturn(requestBook);
//		when(bookServiceClient.getBookById(" Any Token", book.getId())).thenReturn(book);
//		when(authService.getAuthToken()).thenReturn(" Any Token");
//		when(requestRepository.save(Mockito.any())).thenReturn(requestBook);
//		assertThat(provideBookService.addReturnBook(provideBookRequest).getId()).isEqualTo(requestBook.getId());
//	}
//
//	@Test
//	public void testtestProvideBookWithBookNotProvidedException() throws BookNotProvidedException {
//		when(requestBookService.getRequestBook(requestBook.getId())).thenReturn(requestBooks);
//		when(bookServiceClient.getBookById(" Any Token", book.getId())).thenReturn(book);
//		when(authService.getAuthToken()).thenReturn(" Any Token");
//		Exception exception = assertThrows(BookNotProvidedException.class,
//				() -> provideBookService.addReturnBook(provideBookRequest));
//		assertThat(Constants.INVALID_BOOK_REJECTED).isEqualTo(exception.getMessage());
//	}
//
////	public class UserTest {
////		@Test
////		public void testUserNameTooShort() {
////			try {
////		User user = new User();
////user.setName("Jo");
////fail();
////			} catch (IllegalArgumentException ex) {
////				assertEquals("Username is too short", ex.getMessage());
////			}
////		}
////	}
//
////	@DisplayName("test updateSiteDetails success")
////	public void updateSiteDetailsTestSuccess() {
////	when(siteOnboardingRepository.findById(Mockito.anyString()))
////	.thenReturn(Optional.ofNullable(SiteDataBuilder.getSiteOnboardingDetails()));
////	when(clientOnBoardingRepository.findByClientIdAndStatusAndDeleted(Mockito.anyString(), Mockito.any(),
////	Mockito.anyBoolean())).thenReturn(Optional.ofNullable(DataBuilder.getClientOnboardingDetail()));
////	when(siteOnboardingRepository.save(Mockito.any())).thenReturn(SiteDataBuilder.getSiteOnboardingDetails());
////	List<Users> user = new ArrayList<>();
////	user.add(UserDataBuilder.getUser());
////	Users user2 = UserDataBuilder.getUser();
////	user2.setRoles(null);
////	user.add(user2);
////	Users user3 = UserDataBuilder.getUser();
////	List<RoleOnboardingDetails> roles = new ArrayList<>();
////	roles.add(RoleDataBuilder.getManagerRole());
////	user3.setRoles(roles);
////	user.add(user3);
////	UpdateSiteModel site = SiteDataBuilder.getUpdateSiteModel();
////	site.getSiteOnboardingUpdateRequest().setClientId("clientId");
////	when(userOnboardingService.fetchUserByUserIdsAndClientId(Mockito.anyList(), Mockito.anyString()))
////	.thenReturn(user);
////	when(roleOnboardingRepository.findByClientIdAndRoleIgnoreCaseAndDeleted(Mockito.anyString(),
////	Mockito.anyString(), Mockito.anyBoolean()))
////	.thenReturn(Optional.of(RoleDataBuilder.getRoleOnboarding()));
////	SuccessResponse successResponse = siteOnboardingService.updateSiteDetails(site);
////	assertEquals("test-system-id", successResponse.getId());
////	assertEquals(Constants.SITE_UPDATED, successResponse.getMessage());
////	}
//}
