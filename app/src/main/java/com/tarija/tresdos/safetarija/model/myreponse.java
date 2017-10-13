package com.tarija.tresdos.safetarija.model;

import java.util.List;

/**
 * Created by Tresdos on 10/13/2017.
 */

public class myreponse {
    private long multicast_id;
    public int success;
    private int failure;
    private int canonical_ids;
    private List<Result> results;

    public myreponse(){

    }
    public myreponse(long multicast_id, int success, int failure, int canonical_ids, List<Result> results){
        this.setMulticast_id(multicast_id);
        this.setSuccess(success);
        this.setFailure(failure);
        this.setCanonical_ids(canonical_ids);
        this.setResults(results);
    }

    public long getMulticast_id() {
        return multicast_id;
    }

    public void setMulticast_id(long multicast_id) {
        this.multicast_id = multicast_id;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public int getFailure() {
        return failure;
    }

    public void setFailure(int failure) {
        this.failure = failure;
    }

    public int getCanonical_ids() {
        return canonical_ids;
    }

    public void setCanonical_ids(int canonical_ids) {
        this.canonical_ids = canonical_ids;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }
}
