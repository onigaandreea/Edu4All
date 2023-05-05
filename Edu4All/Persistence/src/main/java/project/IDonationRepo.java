package project;

public interface IDonationRepo extends Repository<Donation,Integer>{
    Donation findByIds(Integer idDonor, Integer idSocialCase);
}
