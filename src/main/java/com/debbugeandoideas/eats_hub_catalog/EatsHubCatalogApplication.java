package com.debbugeandoideas.eats_hub_catalog;

import com.debbugeandoideas.eats_hub_catalog.collections.ReservationCollection;
import com.debbugeandoideas.eats_hub_catalog.collections.RestaurantCollection;
import com.debbugeandoideas.eats_hub_catalog.dtos.Review;
import com.debbugeandoideas.eats_hub_catalog.dtos.responses.ReservationResponse;
import com.debbugeandoideas.eats_hub_catalog.dtos.responses.RestaurantResponse;
import com.debbugeandoideas.eats_hub_catalog.enums.PriceEnum;
import com.debbugeandoideas.eats_hub_catalog.enums.ReservationStatusEnum;
import com.debbugeandoideas.eats_hub_catalog.mappers.ReservationMapper;
import com.debbugeandoideas.eats_hub_catalog.mappers.RestaurantMapper;
import com.debbugeandoideas.eats_hub_catalog.records.Address;
import com.debbugeandoideas.eats_hub_catalog.records.ContactInfo;
import com.debbugeandoideas.eats_hub_catalog.repositories.ReservationRepository;
import com.debbugeandoideas.eats_hub_catalog.services.definitions.ReservationCrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class EatsHubCatalogApplication implements CommandLineRunner {

	@Autowired
	private ReservationMapper reservationMapper;

	@Autowired
	private RestaurantMapper restaurantMapper;

	public static void main(String[] args) {
		SpringApplication.run(EatsHubCatalogApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
//
//
//		Address address = Address.builder()
//				.street("Av. Reforma 123")
//				.city("Ciudad de México")
//				.postalCode("06600")
//				.build();
//
//		ContactInfo contactInfo = new ContactInfo(
//				"555-123-4567",
//				"contacto@restaurante.com",
//				"www.restaurante.com"
//		);
//
//		List<Review> reviews = new ArrayList<>();
//
//		Review review1 = new Review();
//		review1.setCustomerId("customer-789");
//		review1.setCustomerName("Ana López");
//		review1.setRating(5);
//		review1.setComment("Excelente comida y servicio. Muy recomendado!");
//		review1.setTimestamp(Instant.now().minusSeconds(86400));
//
//		Review review2 = new Review();
//		review2.setCustomerId("customer-101");
//		review2.setCustomerName("Carlos Mendoza");
//		review2.setRating(4);
//		review2.setComment("Buena experiencia, la comida estaba deliciosa.");
//		review2.setTimestamp(Instant.now().minusSeconds(172800));
//
//		reviews.add(review1);
//		reviews.add(review2);
//
//		RestaurantCollection restaurant = RestaurantCollection.builder()
//				.id(UUID.randomUUID())
//				.name("La Cocina Mexicana")
//				.capacity(50)
//				.address(address)
//				.cuisineType("Mexicana")
//				.priceRange(PriceEnum.MEDIUM)
//				.openHours("09:00 - 22:00")
//				.logoUrl("https://ejemplo.com/logo.png")
//				.closeAt("22:00")
//				.contactInfo(contactInfo)
//				.reviews(reviews)
//				.build();
//
//		ReservationCollection reservation = ReservationCollection.builder()
//				.id(UUID.randomUUID())
//				.restaurantId(restaurant.getId().toString())
//				.customerId("customer-123")
//				.customerName("Juan Pérez")
//				.customerEmail("juan.perez@email.com")
//				.date("2025-07-15")
//				.time("19:30")
//				.partySize(4)
//				.status(ReservationStatusEnum.PENDING)
//				.notes("Mesa junto a la ventana si es posible")
//				.build();
//
//		System.out.println("Restaurant: " + restaurant);
//		System.out.println("Reservation : " + reservation);
//
//		Mono<RestaurantResponse> restaurantResponse = this.restaurantMapper.toResponseMono(
//				Mono.just(restaurant)
//		);
//
//		restaurantResponse.subscribe(System.out::println);
//
//		Mono<ReservationResponse> reservationResponse = this.reservationMapper.toResponseMono(
//				Mono.just(reservation)
//		);
//
//		reservationResponse.subscribe(System.out::println);
	}

}