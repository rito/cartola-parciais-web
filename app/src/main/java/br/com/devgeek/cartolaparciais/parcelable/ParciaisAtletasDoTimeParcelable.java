package br.com.devgeek.cartolaparciais.parcelable;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by geovannefduarte
 */

public class ParciaisAtletasDoTimeParcelable implements Parcelable {

    private Long timeId;
    private String nomeDoTime;
    private String urlEscudoPng;
    private String nomeDoCartoleiro;
    private String parciaisDoTime;


    public ParciaisAtletasDoTimeParcelable(){
    }


    public ParciaisAtletasDoTimeParcelable(Long timeId, String nomeDoTime, String urlEscudoPng, String nomeDoCartoleiro, String parciaisDoTime){
        this.timeId = timeId;
        this.nomeDoTime = nomeDoTime;
        this.urlEscudoPng = urlEscudoPng;
        this.nomeDoCartoleiro = nomeDoCartoleiro;
        this.parciaisDoTime = parciaisDoTime;
    }


    protected ParciaisAtletasDoTimeParcelable(Parcel in){
        this.timeId = in.readLong();
        this.nomeDoTime = in.readString();
        this.urlEscudoPng = in.readString();
        this.nomeDoCartoleiro = in.readString();
        this.parciaisDoTime = in.readString();
    }


    @Override
    public void writeToParcel(Parcel parcel, int flags){
        parcel.writeLong(timeId);
        parcel.writeString(nomeDoTime);
        parcel.writeString(urlEscudoPng);
        parcel.writeString(nomeDoCartoleiro);
        parcel.writeString(parciaisDoTime);
    }


    @Override
    public int describeContents(){
        return 0;
    }

    public static final Creator<ParciaisAtletasDoTimeParcelable> CREATOR = new Creator<ParciaisAtletasDoTimeParcelable>(){
        @Override
        public ParciaisAtletasDoTimeParcelable createFromParcel(Parcel in){
            return new ParciaisAtletasDoTimeParcelable(in);
        }

        @Override
        public ParciaisAtletasDoTimeParcelable[] newArray(int size){
            return new ParciaisAtletasDoTimeParcelable[size];
        }
    };


    public Long getTimeId(){
        return timeId;
    }
    public void setTimeId(Long timeId){
        this.timeId = timeId;
    }
    public String getNomeDoTime(){
        return nomeDoTime;
    }
    public void setNomeDoTime(String nomeDoTime){
        this.nomeDoTime = nomeDoTime;
    }
    public String getUrlEscudoPng(){
        return urlEscudoPng;
    }
    public void setUrlEscudoPng(String urlEscudoPng){
        this.urlEscudoPng = urlEscudoPng;
    }
    public String getNomeDoCartoleiro(){
        return nomeDoCartoleiro;
    }
    public void setNomeDoCartoleiro(String nomeDoCartoleiro){
        this.nomeDoCartoleiro = nomeDoCartoleiro;
    }
    public String getParciaisDoTime(){
        return parciaisDoTime;
    }
    public void setParciaisDoTime(String parciaisDoTime){
        this.parciaisDoTime = parciaisDoTime;
    }
}