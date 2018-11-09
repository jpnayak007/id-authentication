package io.mosip.registration.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

/**
 * UserBiometric entity details
 * 
 * @author Sravya Surampalli
 * @since 1.0.0
 */
@Entity
@Table(schema = "reg", name = "user_biometric")
public class UserBiometric extends RegistrationCommonFields {

	@EmbeddedId
	private UserBiometricId userBiometricId;
	
	@Lob
	@Column(name = "bio_raw_image", nullable = true, updatable = false)
	private byte[] bioRawImage;
	@Column(name = "bio_minutia", nullable = true, updatable = false)
	private String bioMinutia;
	@Lob
	@Column(name = "bio_iso_image", nullable = true, updatable = false)
	private byte[] bioIsoImage;
	@Column(name = "quality_score", precision = 5, scale = 3, nullable = true, updatable = false)
	private BigDecimal qualityScore;
	@Column(name = "no_of_retry", nullable = true, updatable = false)
	private int numberOfRetry;
	@Column(name = "is_deleted", nullable = true, updatable = false)
	@Type(type = "true_false")
	private Boolean isDeleted;
	@Column(name = "del_dtimes", nullable = true, updatable = false)
	private Timestamp delDtimes;

	@ManyToOne
	@JoinColumn(name = "usr_id", nullable = false, insertable = false, updatable = false)
	private RegistrationUserDetail registrationUserDetail;

	public RegistrationUserDetail getRegistrationUserDetail() {
		return registrationUserDetail;
	}

	public void setRegistrationUserDetail(RegistrationUserDetail registrationUserDetail) {
		this.registrationUserDetail = registrationUserDetail;
	}

	/**
	 * @return the userBiometricId
	 */
	public UserBiometricId getUserBiometricId() {
		return userBiometricId;
	}

	/**
	 * @param userBiometricId
	 *            the userBiometricId to set
	 */
	public void setUserBiometricId(UserBiometricId userBiometricId) {
		this.userBiometricId = userBiometricId;
	}

	/**
	 * @return the bioRawImage
	 */
	public byte[] getBioRawImage() {
		return bioRawImage;
	}

	/**
	 * @param bioRawImage
	 *            the bioRawImage to set
	 */
	public void setBioRawImage(byte[] bioRawImage) {
		this.bioRawImage = bioRawImage;
	}

	/**
	 * @return the bioMinutia
	 */
	public String getBioMinutia() {
		return bioMinutia;
	}

	/**
	 * @param bioMinutia
	 *            the bioMinutia to set
	 */
	public void setBioMinutia(String bioMinutia) {
		this.bioMinutia = bioMinutia;
	}

	/**
	 * @return the bioIsoImage
	 */
	public byte[] getBioIsoImage() {
		return bioIsoImage;
	}

	/**
	 * @param bioIsoImage
	 *            the bioIsoImage to set
	 */
	public void setBioIsoImage(byte[] bioIsoImage) {
		this.bioIsoImage = bioIsoImage;
	}

	/**
	 * @return the qualityScore
	 */
	public BigDecimal getQualityScore() {
		return qualityScore;
	}

	/**
	 * @param qualityScore
	 *            the qualityScore to set
	 */
	public void setQualityScore(BigDecimal qualityScore) {
		this.qualityScore = qualityScore;
	}

	/**
	 * @return the numberOfRetry
	 */
	public int getNumberOfRetry() {
		return numberOfRetry;
	}

	/**
	 * @param numberOfRetry
	 *            the numberOfRetry to set
	 */
	public void setNumberOfRetry(int numberOfRetry) {
		this.numberOfRetry = numberOfRetry;
	}

	/**
	 * @return the isDeleted
	 */
	public Boolean isDeleted() {
		return isDeleted;
	}

	/**
	 * @param isDeleted
	 *            the isDeleted to set
	 */
	public void setDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	/**
	 * @return the delDtimes
	 */
	public Timestamp getDelDtimes() {
		return delDtimes;
	}

	/**
	 * @param delDtimes
	 *            the delDtimes to set
	 */
	public void setDelDtimes(Timestamp delDtimes) {
		this.delDtimes = delDtimes;
	}

}
