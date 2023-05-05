package project;

import java.time.LocalDateTime;
import java.util.Objects;

public class Donation extends Entity<Integer>{

    private Donor donor;
    private SocialCase socialCase;
    private int amount;
    private LocalDateTime donationTime;

    public Donation(Donor donor, SocialCase socialCase, int amount, LocalDateTime donationTime) {
        this.donor = donor;
        this.socialCase = socialCase;
        this.amount = amount;
        this.donationTime = donationTime;
    }

    public Donor getDonor() {
        return donor;
    }

    public void setDonor(Donor donor) {
        this.donor = donor;
    }

    public SocialCase getSocialCase() {
        return socialCase;
    }

    public void setSocialCase(SocialCase socialCase) {
        this.socialCase = socialCase;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public LocalDateTime getDonationTime() {
        return donationTime;
    }

    public void setDonationTime(LocalDateTime donationTime) {
        this.donationTime = donationTime;
    }

    @Override
    public String toString() {
        return "Donation{" +
                "donor=" + donor +
                ", socialCase=" + socialCase +
                ", amount=" + amount +
                ", donationTime=" + donationTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Donation donation = (Donation) o;
        return amount == donation.amount && Objects.equals(donor, donation.donor) && Objects.equals(socialCase, donation.socialCase) && Objects.equals(donationTime, donation.donationTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(donor, socialCase, amount, donationTime);
    }
}
