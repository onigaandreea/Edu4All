package project;

import java.util.Objects;

public class Account extends Entity<Integer> {
    private String accountNr;
    private String CVV;
    private String ExpDate;

    public Account(String accountNr, String CVV, String expDate) {
        this.accountNr = accountNr;
        this.CVV = CVV;
        ExpDate = expDate;
    }

    public String getAccountNr() {
        return accountNr;
    }

    public void setAccountNr(String accountNr) {
        this.accountNr = accountNr;
    }

    public String getCVV() {
        return CVV;
    }

    public void setCVV(String CVV) {
        this.CVV = CVV;
    }

    public String getExpDate() {
        return ExpDate;
    }

    public void setExpDate(String expDate) {
        ExpDate = expDate;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountNr='" + accountNr + '\'' +
                ", CVV='" + CVV + '\'' +
                ", ExpDate='" + ExpDate + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(accountNr, account.accountNr) && Objects.equals(CVV, account.CVV) && Objects.equals(ExpDate, account.ExpDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountNr, CVV, ExpDate);
    }
}
