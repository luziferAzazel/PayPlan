package com.paymentview.payplan.Model;

/**
 * Created by dilek on 26.01.2018.
 */

public class JobProfile {
    private String jobType;
    private int payment;

    public JobProfile() {

    }

    public JobProfile(String jobType, int payment) {
        this.jobType = jobType;
        this.payment = payment;
    }

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }
}
