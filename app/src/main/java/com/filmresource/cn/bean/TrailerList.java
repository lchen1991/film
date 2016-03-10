package com.filmresource.cn.bean;

import java.util.List;

/**
 * Created by ql on 2016/3/10.
 */
public class TrailerList extends BaseEntity {
    public List<Trailer> trailers;

    public List<Trailer> getTrailers() {
        return trailers;
    }

    public TrailerList setTrailers(List<Trailer> trailers) {
        this.trailers = trailers;
        return this;
    }
}
