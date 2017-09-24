package br.com.devgeek.cartolaparciais.parcelable;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by geovannefduarte
 */
public class LigasParcelable implements Parcelable {

    private long ligaId;
    private String ligaSlug;
    private String nomeDaLiga;
    private String urlFlamula;


    public LigasParcelable(){
    }


    public LigasParcelable(long ligaId, String ligaSlug, String nomeDaLiga, String urlFlamula){
        this.ligaId = ligaId;
        this.ligaSlug = ligaSlug;
        this.nomeDaLiga = nomeDaLiga;
        this.urlFlamula = urlFlamula;
    }


    protected LigasParcelable(Parcel in){
        ligaId = in.readLong();
        ligaSlug = in.readString();
        nomeDaLiga = in.readString();
        urlFlamula = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags){
        dest.writeLong(ligaId);
        dest.writeString(ligaSlug);
        dest.writeString(nomeDaLiga);
        dest.writeString(urlFlamula);
    }

    @Override
    public int describeContents(){
        return 0;
    }

    public static final Creator<LigasParcelable> CREATOR = new Creator<LigasParcelable>(){
        @Override
        public LigasParcelable createFromParcel(Parcel in){
            return new LigasParcelable(in);
        }

        @Override
        public LigasParcelable[] newArray(int size){
            return new LigasParcelable[size];
        }
    };


    public long getLigaId(){
        return ligaId;
    }
    public void setLigaId(long ligaId){
        this.ligaId = ligaId;
    }
    public String getLigaSlug(){
        return ligaSlug;
    }
    public void setLigaSlug(String ligaSlug){
        this.ligaSlug = ligaSlug;
    }
    public String getNomeDaLiga(){
        return nomeDaLiga;
    }
    public void setNomeDaLiga(String nomeDaLiga){
        this.nomeDaLiga = nomeDaLiga;
    }
    public String getUrlFlamula(){
        return urlFlamula;
    }
    public void setUrlFlamula(String urlFlamula){
        this.urlFlamula = urlFlamula;
    }
}