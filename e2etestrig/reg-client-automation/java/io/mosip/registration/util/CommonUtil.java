package io.mosip.registration.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.mosip.kernel.core.idgenerator.spi.RidGenerator;
import io.mosip.registration.constants.RegistrationConstants;
import io.mosip.registration.context.ApplicationContext;
import io.mosip.registration.context.SessionContext;
import io.mosip.registration.dto.ErrorResponseDTO;
import io.mosip.registration.dto.OSIDataDTO;
import io.mosip.registration.dto.RegistrationCenterDetailDTO;
import io.mosip.registration.dto.RegistrationDTO;
import io.mosip.registration.dto.RegistrationMetaDataDTO;
import io.mosip.registration.dto.ResponseDTO;
import io.mosip.registration.dto.SuccessResponseDTO;
import io.mosip.registration.dto.biometric.BiometricDTO;
import io.mosip.registration.dto.biometric.BiometricInfoDTO;
import io.mosip.registration.dto.biometric.FaceDetailsDTO;
import io.mosip.registration.dto.biometric.FingerprintDetailsDTO;
import io.mosip.registration.dto.biometric.IrisDetailsDTO;
import io.mosip.registration.dto.demographic.ApplicantDocumentDTO;
import io.mosip.registration.dto.demographic.DemographicDTO;
import io.mosip.registration.dto.demographic.DemographicInfoDTO;
import io.mosip.registration.dto.demographic.Identity;
import io.mosip.registration.exception.RegBaseCheckedException;
import io.mosip.registration.service.packet.PacketHandlerService;
import io.mosip.registration.service.sync.PreRegistrationDataSyncService;
import io.mosip.registration.test.util.datastub.DataProvider;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
@Service
public class CommonUtil {
	@Autowired
	PreRegistrationDataSyncService preRegistrationDataSyncService;
	@Autowired
	PacketHandlerService packetHandlerService;

	@Autowired
	RidGenerator<String> ridGeneratorImpl;

	ApplicationContext applicationContext = ApplicationContext.getInstance();

	/**
	 * Declaring CenterID,StationID global
	 */
	private static String centerID = null;
	private static String stationID = null;

	private static Logger logger = Logger.getLogger(CommonUtil.class);

	public HashMap<String, String> getPreRegIDs(){
		ObjectMapper mapper = new ObjectMapper();
		Object obj;
	HashMap<String, String> response=new HashMap<String, String>();
	
	/**
     * Read JSON from a file into a Map
     */
    try {
        response= mapper.readValue(new File(
                "result.json"), new TypeReference<Map<String, String>>() {
        });
        
		obj = new JSONParser().parse(new FileReader("src/main/resources/PreId.json"));
		JSONObject jo = (JSONObject) obj;
		String adultID = (String) jo.get("PrIdOfAdultWithDocs");
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return response;
	
	}

	public RegistrationDTO getPreRegistrationDetails(String preRegId) {

		ResponseDTO responseDTO = preRegistrationDataSyncService.getPreRegistration(preRegId);
		RegistrationDTO registrationDTO = new RegistrationDTO();
		SuccessResponseDTO successResponseDTO = responseDTO.getSuccessResponseDTO();
		List<ErrorResponseDTO> errorResponseDTOList = responseDTO.getErrorResponseDTOs();

		if (successResponseDTO != null && successResponseDTO.getOtherAttributes() != null
				&& successResponseDTO.getOtherAttributes().containsKey(RegistrationConstants.REGISTRATION_DTO)) {
			SessionContext.map().put(RegistrationConstants.REGISTRATION_DATA,
					successResponseDTO.getOtherAttributes().get(RegistrationConstants.REGISTRATION_DTO));
			System.out.println(successResponseDTO.getMessage());
			registrationDTO = (RegistrationDTO) successResponseDTO.getOtherAttributes()
					.get(RegistrationConstants.REGISTRATION_DTO);

		} else if (errorResponseDTOList != null && !errorResponseDTOList.isEmpty()) {
			logger.info(RegistrationConstants.ERROR + errorResponseDTOList.get(0).getMessage());
		}
		return registrationDTO;
	}

	public static final String THUMB_JPG = "/thumb.jpg";

	public static List<FingerprintDetailsDTO> getFingerprintDetailsDTO(String personType)
			throws RegBaseCheckedException {
		List<FingerprintDetailsDTO> fingerList = new ArrayList<>();

		if (personType.equals("applicant")) {
			FingerprintDetailsDTO fingerprint = buildFingerPrintDetailsDTO(THUMB_JPG, "BothThumbs.jpg", 85.0, false,
					"thumbs", 0);
			fingerprint.getSegmentedFingerprints()
					.add(buildFingerPrintDetailsDTO(THUMB_JPG, "rightThumb.jpg", 80.0, false, "rightThumb", 2));
			fingerList.add(fingerprint);

			fingerprint = buildFingerPrintDetailsDTO(THUMB_JPG, "LeftPalm.jpg", 80.0, false, "leftSlap", 3);
			fingerprint.getSegmentedFingerprints()
					.add(buildFingerPrintDetailsDTO(THUMB_JPG, "leftIndex.jpg", 80.0, false, "leftIndex", 3));
			fingerprint.getSegmentedFingerprints()
					.add(buildFingerPrintDetailsDTO(THUMB_JPG, "leftMiddle.jpg", 80.0, false, "leftMiddle", 1));
			fingerprint.getSegmentedFingerprints()
					.add(buildFingerPrintDetailsDTO(THUMB_JPG, "leftRing.jpg", 80.0, false, "leftRing", 2));
			fingerprint.getSegmentedFingerprints()
					.add(buildFingerPrintDetailsDTO(THUMB_JPG, "leftLittle.jpg", 80.0, false, "leftLittle", 0));
			fingerList.add(fingerprint);

			fingerprint = buildFingerPrintDetailsDTO(THUMB_JPG, "RightPalm.jpg", 95.0, false, "rightSlap", 2);
			fingerprint.getSegmentedFingerprints()
					.add(buildFingerPrintDetailsDTO(THUMB_JPG, "rightIndex.jpg", 80.0, false, "rightIndex", 3));
			fingerprint.getSegmentedFingerprints()
					.add(buildFingerPrintDetailsDTO(THUMB_JPG, "rightMiddle.jpg", 80.0, false, "rightMiddle", 1));
			fingerprint.getSegmentedFingerprints()
					.add(buildFingerPrintDetailsDTO(THUMB_JPG, "rightRing.jpg", 80.0, false, "rightRing", 2));
			fingerprint.getSegmentedFingerprints()
					.add(buildFingerPrintDetailsDTO(THUMB_JPG, "rightLittle.jpg", 80.0, false, "rightLittle", 0));
			fingerList.add(fingerprint);
		} else {
			fingerList
					.add(buildFingerPrintDetailsDTO(THUMB_JPG, personType + "LeftThumb.jpg", 0, false, "leftThumb", 0));
		}

		return fingerList;
	}

	private static FingerprintDetailsDTO buildFingerPrintDetailsDTO(String imageLoc, String fingerprintImageName,
			double qualityScore, boolean isForceCaptured, String fingerType, int numRetry)
			// buildFingerPrintDetailsDTO(THUMB_JPG,
			// "rightIndex.jpg", 80.0, false, "rightIndex", 3
			//
			throws RegBaseCheckedException {
		FingerprintDetailsDTO fingerprintDetailsDTO = new FingerprintDetailsDTO();
		fingerprintDetailsDTO.setFingerPrint(getImageBytes(imageLoc));
		fingerprintDetailsDTO.setFingerprintImageName(fingerprintImageName);
		fingerprintDetailsDTO.setQualityScore(qualityScore);
		fingerprintDetailsDTO.setForceCaptured(isForceCaptured);
		fingerprintDetailsDTO.setFingerType(fingerType);
		fingerprintDetailsDTO.setNumRetry(numRetry);
		fingerprintDetailsDTO.setSegmentedFingerprints(new ArrayList<>());
		return fingerprintDetailsDTO;
	}

	public static byte[] getImageBytes(String filePath) throws RegBaseCheckedException {
		filePath = "/dataprovider".concat(filePath);

		try {
			InputStream file = CommonUtil.class.getResourceAsStream(filePath);
			byte[] bytesArray = new byte[(int) file.available()];
			file.read(bytesArray);
			file.close();

			return bytesArray;
		} catch (IOException ioException) {
			throw new RegBaseCheckedException(RegistrationConstants.SERVICE_DATA_PROVIDER_UTIL,
					"Unable to read the Image bytes", ioException);
		}
	}

	public RegistrationDTO setRegistrationClientRegDTO(RegistrationDTO preRegistrationDTO) {
		HashMap<String, String> returnValues = new HashMap<>();
		RegistrationDTO registrationDTO = preRegistrationDTO;
		// set FingerprintDetailsDTO
		List<FingerprintDetailsDTO> fpData = null;
		try {
			fpData = getFingerprintDetailsDTO("applicant");
		} catch (RegBaseCheckedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// getFingerPrintTestData(fpPath);
		// set IrisDetailsDTO
		String irisPath = "src/main/resources/RegistrationClient_Packet_TestData/IrisrDataDetails.json";
		List<IrisDetailsDTO> irisData = getIrisTestData(irisPath);

		// if
		// (registrationDTO.getBiometricDTO().getApplicantBiometricDTO().getFingerprintDetailsDTO()
		// == null) {
		registrationDTO.getBiometricDTO().getApplicantBiometricDTO().setFingerprintDetailsDTO(fpData);
		registrationDTO.getBiometricDTO().getApplicantBiometricDTO().getFingerprintDetailsDTO();
		// }
		// if
		// (registrationDTO.getBiometricDTO().getApplicantBiometricDTO().getIrisDetailsDTO()
		// == null) {
		registrationDTO.getBiometricDTO().getApplicantBiometricDTO().setIrisDetailsDTO(irisData);
		// }
		return registrationDTO;
	}

	/**
	 * 
	 * @param Path
	 *            - To fetch the Finger print byte array from json file
	 * @return - the list of FingerPrint details
	 * @throws IOException
	 * @throws ParseException
	 */
	public static List<FingerprintDetailsDTO> getFingerPrintTestData(String Path) {

		List<FingerprintDetailsDTO> fingerprintdetailsData = new ArrayList<>();
		try {
			ObjectMapper mapper = new ObjectMapper();
			JSONParser jsonParser = new JSONParser();
			FileReader reader = new FileReader(Path);
			// Read JSON file
			Object obj = jsonParser.parse(reader);

			JSONArray jArray = (JSONArray) obj;

			String s = jArray.toString();
			fingerprintdetailsData = mapper.readValue(s,
					mapper.getTypeFactory().constructCollectionType(List.class, FingerprintDetailsDTO.class));

		} catch (IOException | ParseException e) {
			// TODO: handle exception
		}
		return fingerprintdetailsData;

	}

	/**
	 * 
	 * @param Path
	 *            - To fetch the Finger print byte array from json file
	 * @return - the list of FingerPrint details
	 * @throws IOException
	 * @throws ParseException
	 */
	public static List<IrisDetailsDTO> getIrisTestData(String Path) {

		List<IrisDetailsDTO> irisData = new ArrayList<>();
		try {
			ObjectMapper mapper = new ObjectMapper();
			JSONParser jsonParser = new JSONParser();
			FileReader reader = new FileReader(Path);
			// Read JSON file
			Object obj = jsonParser.parse(reader);

			JSONArray jArray = (JSONArray) obj;

			String s = jArray.toString();
			irisData = mapper.readValue(s,
					mapper.getTypeFactory().constructCollectionType(List.class, IrisDetailsDTO.class));

		} catch (IOException | ParseException e) {
			// TODO: handle exception
		}
		return irisData;

	}

	/**
	 * This method will create registration DTO object
	 */
	public RegistrationDTO createRegistrationDTOObject(String registrationCategory, String centerID, String stationID) {
		RegistrationDTO registrationDTO = new RegistrationDTO();

		// Create objects for Biometric DTOS
		BiometricDTO biometricDTO = new BiometricDTO();
		biometricDTO.setApplicantBiometricDTO(createBiometricInfoDTO());
		biometricDTO.setIntroducerBiometricDTO(createBiometricInfoDTO());
		biometricDTO.setOperatorBiometricDTO(createBiometricInfoDTO());
		biometricDTO.setSupervisorBiometricDTO(createBiometricInfoDTO());
		registrationDTO.setBiometricDTO(biometricDTO);

		// Create object for Demographic DTOS
		DemographicDTO demographicDTO = new DemographicDTO();
		ApplicantDocumentDTO applicantDocumentDTO = new ApplicantDocumentDTO();
		applicantDocumentDTO.setDocuments(new HashMap<>());

		demographicDTO.setApplicantDocumentDTO(applicantDocumentDTO);
		DemographicInfoDTO demographicInfoDTO = new DemographicInfoDTO();
		Identity identity = new Identity();
		demographicInfoDTO.setIdentity(identity);
		demographicDTO.setDemographicInfoDTO(demographicInfoDTO);

		applicantDocumentDTO.setDocuments(new HashMap<>());

		registrationDTO.setDemographicDTO(demographicDTO);

		// Create object for OSIData DTO
		registrationDTO.setOsiDataDTO(new OSIDataDTO());

		// Create object for RegistrationMetaData DTO
		RegistrationMetaDataDTO registrationMetaDataDTO = new RegistrationMetaDataDTO();
		registrationMetaDataDTO.setRegistrationCategory(registrationCategory);

		RegistrationCenterDetailDTO registrationCenter = SessionContext.userContext().getRegistrationCenterDetailDTO();

		if (RegistrationConstants.ENABLE.equalsIgnoreCase(
				(String) ApplicationContext.map().get(RegistrationConstants.GPS_DEVICE_DISABLE_FLAG))) {
			registrationMetaDataDTO
					.setGeoLatitudeLoc(Double.parseDouble(registrationCenter.getRegistrationCenterLatitude()));
			registrationMetaDataDTO
					.setGeoLongitudeLoc(Double.parseDouble(registrationCenter.getRegistrationCenterLongitude()));
		}

		Map<String, Object> applicationContextMap = ApplicationContext.map();

		registrationMetaDataDTO.setCenterId((String) applicationContextMap.get(RegistrationConstants.USER_CENTER_ID));
		registrationMetaDataDTO.setMachineId((String) applicationContextMap.get(RegistrationConstants.USER_STATION_ID));
		registrationMetaDataDTO
				.setDeviceId((String) applicationContextMap.get(RegistrationConstants.DONGLE_SERIAL_NUMBER));

		registrationDTO.setRegistrationMetaDataDTO(registrationMetaDataDTO);

		// Set RID
		String registrationID = ridGeneratorImpl.generateId(centerID, stationID);

		logger.info("Registration Started for RID  : [ " + registrationID + " ] ");

		registrationDTO.setRegistrationId(registrationID);
		// Put the RegistrationDTO object to SessionContext Map

		SessionContext.map().put(RegistrationConstants.REGISTRATION_DATA, registrationDTO);
		return registrationDTO;
	}

	/**
	 * This method will create the biometrics info DTO
	 */
	public BiometricInfoDTO createBiometricInfoDTO() {
		BiometricInfoDTO biometricInfoDTO = new BiometricInfoDTO();
		biometricInfoDTO.setBiometricExceptionDTO(new ArrayList<>());
		biometricInfoDTO.setFingerprintDetailsDTO(new ArrayList<>());
		biometricInfoDTO.setIrisDetailsDTO(new ArrayList<>());
		biometricInfoDTO.setFace(new FaceDetailsDTO());
		biometricInfoDTO.setExceptionFace(new FaceDetailsDTO());
		return biometricInfoDTO;
	}

}
