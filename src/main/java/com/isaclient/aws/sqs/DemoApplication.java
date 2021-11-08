package com.isaclient.aws.sqs;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isaclient.aws.sqs.model.EVChargingLocation;
import com.isaclient.aws.sqs.model.EVChargingLocationVO;
import com.isaclient.aws.sqs.model.EVServiceEquipment;
import com.isaclient.aws.sqs.model.ElectricVehicleConnector;
import com.isaclient.aws.sqs.repository.EVChargingLocationRepository;
import com.isaclient.aws.sqs.repository.EVServiceEquipmentRepository;
import com.isaclient.aws.sqs.repository.EvConnectorRepository;
import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@RestController
@RequestMapping("/Ev")
public class DemoApplication<retries> {

	Logger logger = LoggerFactory.getLogger(DemoApplication.class);

	@Autowired
	private QueueMessagingTemplate queueMessagingTemplate;

	@Autowired
	private AmazonSQSAsync sqs;

	@Value("${max.messages.per.pull}")
	private int maxNumberOfMessages;

	@Autowired
	private EvConnectorRepository evConnectorRepository;
	@Autowired
	private EVChargingLocationRepository evChargingLocationRepository;
	@Autowired
	private EVServiceEquipmentRepository evServiceEquipmentRepository;

	@Value("$(cloud.aws.end-point.uri)")
	private String endpoint;

	@PostMapping (value = "/Connector")
	public void sendMessageConnector(@RequestBody ElectricVehicleConnector evConnector){
		queueMessagingTemplate.convertAndSend("EVConnector" , evConnector);
		logger.info("Message sent successfully");
	}

	//@SqsListener("EVConnector")
	@GetMapping(value = "/Connector/pull/message")
	public void loadMessageConnector(String message){
		logger.info("Message received");
		ObjectMapper mapper = new ObjectMapper();
		List<ElectricVehicleConnector> connectors = new ArrayList<>();

			List<Message> messages = getMessages("http://localhost:4566/000000000000/EVConnector");
			messages.forEach(msg -> {

				String messageBody = msg.getBody();

				try {
					connectors.add(mapper.readValue(messageBody, ElectricVehicleConnector.class));
				}  catch (JsonMappingException e) {
					logger.error(e.getMessage());
				} catch (JsonProcessingException e) {
					logger.error(e.getMessage());
				}
			});
			evConnectorRepository.saveAll(connectors);
	}


	@PostMapping (value = "/ChargingLocation")
	public void sendMessageChargingLocation(@RequestBody EVChargingLocationVO evChloc){
		queueMessagingTemplate.convertAndSend("EVChargingLocation" , evChloc);
		logger.info("Message sent successfully");
	}

	//@SqsListener("EVChargingLocation")
	@GetMapping(value = "/ChargingLocation/pull/message")
	public void loadMessageChargingLocation(String message) {
		logger.info("Message received");
		ObjectMapper mapper = new ObjectMapper();
		List<EVChargingLocation> locations = new ArrayList<>();

		List<Message> messages = getMessages("http://localhost:4566/000000000000/EVChargingLocation");
		messages.forEach(msg -> {

				String messageBody = msg.getBody();

				try {
					EVChargingLocationVO evChargingLocationVO = mapper.readValue(messageBody, EVChargingLocationVO.class);
					EVChargingLocation evChargingLocation = convertToEVChargingLocation(evChargingLocationVO);
					locations.add(evChargingLocation);
				} catch (ParseException e) {
					logger.error(e.getMessage());
				} catch (JsonMappingException e) {
					logger.error(e.getMessage());
				} catch (JsonProcessingException e) {
					logger.error(e.getMessage());
				}
			});
		evChargingLocationRepository.saveAll(locations);
	}

	private List<Message> getMessages(String queueURL) {
		ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueURL)
				.withWaitTimeSeconds(10)
				.withMaxNumberOfMessages(maxNumberOfMessages);
		List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();
		return messages;
	}

	private EVChargingLocation convertToEVChargingLocation(EVChargingLocationVO evChargingLocationVO) throws ParseException {
		EVChargingLocation evChargingLocation = new EVChargingLocation();
		evChargingLocation.set_id(evChargingLocationVO.get_id());
		evChargingLocation.setLastUpdated(evChargingLocationVO.getLastUpdated());
		evChargingLocation.set_status(evChargingLocationVO.get_status());
		evChargingLocation.setAccess(evChargingLocationVO.getAccess());
		evChargingLocation.setAddress(evChargingLocationVO.getAddress());
		evChargingLocation.setCity(evChargingLocationVO.getCity());
		evChargingLocation.setCountryCode(evChargingLocationVO.getCountryCode());
		evChargingLocation.setCountryCodeIso(evChargingLocationVO.getCountryCodeIso());
		evChargingLocation.setCountryName(evChargingLocationVO.getCountryName());
		evChargingLocation.setDescription(evChargingLocationVO.getDescription());
		evChargingLocation.setFacilities(evChargingLocationVO.getFacilities());
		evChargingLocation.setName(evChargingLocationVO.getName());
		evChargingLocation.setParkingType(evChargingLocationVO.getParkingType());
		evChargingLocation.setPublish(evChargingLocationVO.getPublish());
		evChargingLocation.setPostalCode(evChargingLocationVO.getPostalCode());
		evChargingLocation.setStateCode(evChargingLocationVO.getStateCode());
		List<String> coordinates = evChargingLocationVO.getGeoPoint().getCoordinates();
		StringBuilder sb = new StringBuilder();
		sb.append(evChargingLocationVO.getGeoPoint().getType());
		sb.append(" ");
		sb.append("(");
		sb.append(coordinates.get(0));
		sb.append(" ");
		sb.append(coordinates.get(1));
		sb.append(")");

		GeometryFactory factory = new GeometryFactory(new PrecisionModel(), 4326);
		double lat = Double.parseDouble(coordinates.get(0));
		double lon = Double.parseDouble(coordinates.get(1));
		evChargingLocation.setPoint(factory.createPoint(new Coordinate(lat , lon)));
		evChargingLocation.setStateName(evChargingLocationVO.getStateName());
		return evChargingLocation;
	}

	@PostMapping (value = "/ServiceEquipment")
	public void sendMessageServiceEquipment(@RequestBody EVServiceEquipment evService){
		queueMessagingTemplate.convertAndSend("EVServiceEquipment" , evService);
		logger.info("Message sent successfully");
	}

	//@SqsListener("EVServiceEquipment")
	@GetMapping(value = "/ServiceEquipment/pull/message")
	public void loadMessageServiceEquipment(String message){
		logger.info("Message received");
		ObjectMapper mapper = new ObjectMapper();

		List<EVServiceEquipment> equipments = new ArrayList<>();

		List<Message> messages = getMessages("http://localhost:4566/000000000000/EVServiceEquipment");
		messages.forEach(msg -> {

			String messageBody = msg.getBody();
			try {
				equipments.add(mapper.readValue(messageBody, EVServiceEquipment.class));
			}  catch (JsonMappingException e) {
				logger.error(e.getMessage());
			} catch (JsonProcessingException e) {
				logger.error(e.getMessage());
			}
		});
		evServiceEquipmentRepository.saveAll(equipments);
	}


	@GetMapping(value = "/location/name")
	public void findByName(@RequestParam ("name") String name){
		EVChargingLocation location = evChargingLocationRepository.findByName(name);
		logger.info(location._id);
	}
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}





}
