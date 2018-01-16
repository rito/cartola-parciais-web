package br.com.devgeek.cartolaparciais.util;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.SuperscriptSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import br.com.devgeek.cartolaparciais.R;
import br.com.devgeek.cartolaparciais.model.AtletasPontuados;

import static br.com.devgeek.cartolaparciais.util.CartolaParciaisUtil.loadAtletaImageWithPicasso;

/**
 * Created by geovannefduarte
 */

public class PosicoesJogadoresUtil {

    public static final int GOLEIRO  = 1;
    public static final int LATERAL  = 2;
    public static final int ZAGUEIRO = 3;
    public static final int MEIA     = 4;
    public static final int ATACANTE = 5;
    public static final int TECNICO  = 6;

    public static String getPosicaoNome(int posicao){

        switch (posicao){
            case GOLEIRO:   return "Goleiro";
            case LATERAL:   return "Lateral";
            case ZAGUEIRO:  return "Zagueiro";
            case MEIA:      return "Meia";
            case ATACANTE:  return "Atacante";
            case TECNICO:   return "Técnico";
            default: return "";
        }
    }

    public static String getPosicaoAbreviada(int posicao){

        switch (posicao){
            case GOLEIRO:   return "gol";
            case LATERAL:   return "lat";
            case ZAGUEIRO:  return "zag";
            case MEIA:      return "mei";
            case ATACANTE:  return "ata";
            case TECNICO:   return "tec";
            default: return "";
        }
    }

    private static String getTimeFormacao(List<AtletasPontuados> atletasPontuados){

        String formacao = "";
        int goleiro = 0, lateral = 0, zagueiro = 0, meia = 0, atacante = 0, tecnico = 0;

        for (AtletasPontuados atleta : atletasPontuados){

            switch (atleta.getPosicaoId()){
                case GOLEIRO:   goleiro++;  break;
                case LATERAL:   lateral++;  break;
                case ZAGUEIRO:  zagueiro++; break;
                case MEIA:      meia++;     break;
                case ATACANTE:  atacante++; break;
                case TECNICO:   tecnico++;  break;
                default: break;
            }
        }

        formacao = String.valueOf( (lateral + zagueiro) ) + "-" + String.valueOf( meia ) + "-" + String.valueOf( atacante );
        return formacao;
    }

    public static void getTimeFormacaoAndShowInField(Context context, View view, List<AtletasPontuados> atletasPontuados, DecimalFormat formatoPontuacao){

        String formacao[] = getTimeFormacao(atletasPontuados).split("-");
        Integer qtdeAtacantes = Integer.valueOf(formacao[2]), qtdeMeias = Integer.valueOf(formacao[1]), qtdeZagueiros = Integer.valueOf(formacao[0]);

        ImageView tecnico = (ImageView) view.findViewById(R.id.tecnico);
        ImageView tecnicoEscudo = (ImageView) view.findViewById(R.id.tecnicoEscudo);
        ImageView tecnicoVender = (ImageView) view.findViewById(R.id.tecnicoVender);
        TextView tecnicoPontuacao = (TextView) view.findViewById(R.id.tecnicoPontuacao);

        ImageView goleiro = (ImageView) view.findViewById(R.id.goleiro);
        ImageView goleiroEscudo = (ImageView) view.findViewById(R.id.goleiroEscudo);
        ImageView goleiroVender = (ImageView) view.findViewById(R.id.goleiroVender);
        TextView goleiroPontuacao = (TextView) view.findViewById(R.id.goleiroPontuacao);

        boolean atacanteEsquerdaEmCampo = false;
        ImageView atacanteEsquerda = (ImageView) view.findViewById(R.id.atacanteEsquerda);
        ImageView atacanteEsquerdaEscudo = (ImageView) view.findViewById(R.id.atacanteEsquerdaEscudo);
        ImageView atacanteEsquerdaVender = (ImageView) view.findViewById(R.id.atacanteEsquerdaVender);
        TextView atacanteEsquerdaPontuacao = (TextView) view.findViewById(R.id.atacanteEsquerdaPontuacao);
        RelativeLayout atacanteEsquerdaContent = (RelativeLayout) view.findViewById(R.id.atacanteEsquerdaContent);

        boolean atacanteCentroEmCampo = false;
        ImageView atacanteCentro = (ImageView) view.findViewById(R.id.atacanteCentro);
        ImageView atacanteCentroEscudo = (ImageView) view.findViewById(R.id.atacanteCentroEscudo);
        ImageView atacanteCentroVender = (ImageView) view.findViewById(R.id.atacanteCentroVender);
        TextView atacanteCentroPontuacao = (TextView) view.findViewById(R.id.atacanteCentroPontuacao);
        RelativeLayout atacanteCentroContent = (RelativeLayout) view.findViewById(R.id.atacanteCentroContent);

        boolean atacanteDireitaEmCampo = false;
        ImageView atacanteDireita = (ImageView) view.findViewById(R.id.atacanteDireita);
        ImageView atacanteDireitaEscudo = (ImageView) view.findViewById(R.id.atacanteDireitaEscudo);
        ImageView atacanteDireitaVender = (ImageView) view.findViewById(R.id.atacanteDireitaVender);
        TextView atacanteDireitaPontuacao = (TextView) view.findViewById(R.id.atacanteDireitaPontuacao);
        RelativeLayout atacanteDireitaContent = (RelativeLayout) view.findViewById(R.id.atacanteDireitaContent);

        if (qtdeAtacantes == 1){

            atacanteEsquerdaContent.setVisibility(View.GONE);
            atacanteCentroContent.setVisibility(View.VISIBLE);
            atacanteDireitaContent.setVisibility(View.GONE);

        } else if (qtdeAtacantes == 2) {

            atacanteEsquerdaContent.setVisibility(View.VISIBLE);
            atacanteCentroContent.setVisibility(View.GONE);
            atacanteDireitaContent.setVisibility(View.VISIBLE);

        } else if (qtdeAtacantes == 3){

            atacanteEsquerdaContent.setVisibility(View.VISIBLE);
            atacanteCentroContent.setVisibility(View.VISIBLE);
            atacanteDireitaContent.setVisibility(View.VISIBLE);
        }

        boolean pontaEsquerdoEmCampo = false;
        ImageView pontaEsquerdo = (ImageView) view.findViewById(R.id.pontaEsquerdo);
        ImageView pontaEsquerdoEscudo = (ImageView) view.findViewById(R.id.pontaEsquerdoEscudo);
        ImageView pontaEsquerdoVender = (ImageView) view.findViewById(R.id.pontaEsquerdoVender);
        TextView pontaEsquerdoPontuacao = (TextView) view.findViewById(R.id.pontaEsquerdoPontuacao);
        RelativeLayout pontaEsquerdoContent = (RelativeLayout) view.findViewById(R.id.pontaEsquerdoContent);

        boolean meiaEsquerdoEmCampo = false;
        ImageView meiaEsquerdo = (ImageView) view.findViewById(R.id.meiaEsquerdo);
        ImageView meiaEsquerdoEscudo = (ImageView) view.findViewById(R.id.meiaEsquerdoEscudo);
        ImageView meiaEsquerdoVender = (ImageView) view.findViewById(R.id.meiaEsquerdoVender);
        TextView meiaEsquerdoPontuacao = (TextView) view.findViewById(R.id.meiaEsquerdoPontuacao);
        RelativeLayout meiaEsquerdoContent = (RelativeLayout) view.findViewById(R.id.meiaEsquerdoContent);

        boolean volanteEmCampo = false;
        ImageView volante = (ImageView) view.findViewById(R.id.volante);
        ImageView volanteEscudo = (ImageView) view.findViewById(R.id.volanteEscudo);
        ImageView volanteVender = (ImageView) view.findViewById(R.id.volanteVender);
        TextView volantePontuacao = (TextView) view.findViewById(R.id.volantePontuacao);
        RelativeLayout volanteContent = (RelativeLayout) view.findViewById(R.id.volanteContent);

        boolean meiaDireitoEmCampo = false;
        ImageView meiaDireito = (ImageView) view.findViewById(R.id.meiaDireito);
        ImageView meiaDireitoEscudo = (ImageView) view.findViewById(R.id.meiaDireitoEscudo);
        ImageView meiaDireitoVender = (ImageView) view.findViewById(R.id.meiaDireitoVender);
        TextView meiaDireitoPontuacao = (TextView) view.findViewById(R.id.meiaDireitoPontuacao);
        RelativeLayout meiaDireitoContent = (RelativeLayout) view.findViewById(R.id.meiaDireitoContent);

        boolean pontaDireitoEmCampo = false;
        ImageView pontaDireito = (ImageView) view.findViewById(R.id.pontaDireito);
        ImageView pontaDireitoEscudo = (ImageView) view.findViewById(R.id.pontaDireitoEscudo);
        ImageView pontaDireitoVender = (ImageView) view.findViewById(R.id.pontaDireitoVender);
        TextView pontaDireitoPontuacao = (TextView) view.findViewById(R.id.pontaDireitoPontuacao);
        RelativeLayout pontaDireitoContent = (RelativeLayout) view.findViewById(R.id.pontaDireitoContent);

        if (qtdeMeias == 3){

            pontaEsquerdoContent.setVisibility(View.GONE);
            meiaEsquerdoContent.setVisibility(View.VISIBLE);
            volanteContent.setVisibility(View.VISIBLE);
            meiaDireitoContent.setVisibility(View.VISIBLE);
            pontaDireitoContent.setVisibility(View.GONE);

        } else if (qtdeMeias == 4){

            pontaEsquerdoContent.setVisibility(View.VISIBLE);
            meiaEsquerdoContent.setVisibility(View.VISIBLE);
            volanteContent.setVisibility(View.GONE);
            meiaDireitoContent.setVisibility(View.VISIBLE);
            pontaDireitoContent.setVisibility(View.VISIBLE);

        } else if (qtdeMeias == 5){

            pontaEsquerdoContent.setVisibility(View.VISIBLE);
            meiaEsquerdoContent.setVisibility(View.VISIBLE);
            volanteContent.setVisibility(View.VISIBLE);
            meiaDireitoContent.setVisibility(View.VISIBLE);
            pontaDireitoContent.setVisibility(View.VISIBLE);
        }

        boolean lateralEsquerdoEmCampo = false;
        ImageView lateralEsquerdo = (ImageView) view.findViewById(R.id.lateralEsquerdo);
        ImageView lateralEsquerdoEscudo = (ImageView) view.findViewById(R.id.lateralEsquerdoEscudo);
        ImageView lateralEsquerdoVender = (ImageView) view.findViewById(R.id.lateralEsquerdoVender);
        TextView lateralEsquerdoPontuacao = (TextView) view.findViewById(R.id.lateralEsquerdoPontuacao);
        RelativeLayout lateralEsquerdoContent = (RelativeLayout) view.findViewById(R.id.lateralEsquerdoContent);

        boolean zagueiroEsquerdoEmCampo = false;
        ImageView zagueiroEsquerdo = (ImageView) view.findViewById(R.id.zagueiroEsquerdo);
        ImageView zagueiroEsquerdoEscudo = (ImageView) view.findViewById(R.id.zagueiroEsquerdoEscudo);
        ImageView zagueiroEsquerdoVender = (ImageView) view.findViewById(R.id.zagueiroEsquerdoVender);
        TextView zagueiroEsquerdoPontuacao = (TextView) view.findViewById(R.id.zagueiroEsquerdoPontuacao);
        RelativeLayout zagueiroEsquerdoContent = (RelativeLayout) view.findViewById(R.id.zagueiroEsquerdoContent);

        boolean zaqueiroCentroEmCampo = false;
        ImageView zaqueiroCentro = (ImageView) view.findViewById(R.id.zaqueiroCentro);
        ImageView zaqueiroCentroEscudo = (ImageView) view.findViewById(R.id.zaqueiroCentroEscudo);
        ImageView zaqueiroCentroVender = (ImageView) view.findViewById(R.id.zaqueiroCentroVender);
        TextView zaqueiroCentroPontuacao = (TextView) view.findViewById(R.id.zaqueiroCentroPontuacao);
        RelativeLayout zaqueiroCentroContent = (RelativeLayout) view.findViewById(R.id.zaqueiroCentroContent);

        boolean zagueiroDireitoEmCampo = false;
        ImageView zagueiroDireito = (ImageView) view.findViewById(R.id.zagueiroDireito);
        ImageView zagueiroDireitoEscudo = (ImageView) view.findViewById(R.id.zagueiroDireitoEscudo);
        ImageView zagueiroDireitoVender = (ImageView) view.findViewById(R.id.zagueiroDireitoVender);
        TextView zagueiroDireitoPontuacao = (TextView) view.findViewById(R.id.zagueiroDireitoPontuacao);
        RelativeLayout zagueiroDireitoContent = (RelativeLayout) view.findViewById(R.id.zagueiroDireitoContent);

        boolean lateralDireitoEmCampo = false;
        ImageView lateralDireito = (ImageView) view.findViewById(R.id.lateralDireito);
        ImageView lateralDireitoEscudo = (ImageView) view.findViewById(R.id.lateralDireitoEscudo);
        ImageView lateralDireitoVender = (ImageView) view.findViewById(R.id.lateralDireitoVender);
        TextView lateralDireitoPontuacao = (TextView) view.findViewById(R.id.lateralDireitoPontuacao);
        RelativeLayout lateralDireitoContent = (RelativeLayout) view.findViewById(R.id.lateralDireitoContent);

        if (qtdeMeias == 3){

            lateralEsquerdoContent.setVisibility(View.GONE);
            zagueiroEsquerdoContent.setVisibility(View.VISIBLE);
            zaqueiroCentroContent.setVisibility(View.VISIBLE);
            zagueiroDireitoContent.setVisibility(View.VISIBLE);
            lateralDireitoContent.setVisibility(View.GONE);

        } else if (qtdeMeias == 4){

            lateralEsquerdoContent.setVisibility(View.VISIBLE);
            zagueiroEsquerdoContent.setVisibility(View.VISIBLE);
            zaqueiroCentroContent.setVisibility(View.GONE);
            zagueiroDireitoContent.setVisibility(View.VISIBLE);
            lateralDireitoContent.setVisibility(View.VISIBLE);

        } else if (qtdeMeias == 5){

            lateralEsquerdoContent.setVisibility(View.VISIBLE);
            zagueiroEsquerdoContent.setVisibility(View.VISIBLE);
            zaqueiroCentroContent.setVisibility(View.VISIBLE);
            zagueiroDireitoContent.setVisibility(View.VISIBLE);
            lateralDireitoContent.setVisibility(View.VISIBLE);
        }



        SpannableStringBuilder pontuacaoFormatada;
        for (AtletasPontuados atleta : atletasPontuados){

            pontuacaoFormatada = new SpannableStringBuilder(formatoPontuacao.format(atleta.getPontuacao()));
            pontuacaoFormatada.setSpan(new SuperscriptSpan(), 0, pontuacaoFormatada.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            pontuacaoFormatada.setSpan(new RelativeSizeSpan(0.65f), 0, pontuacaoFormatada.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            if (atleta.getPontuacao() > 0){
                pontuacaoFormatada.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.cartoletaPositiva)), 0, pontuacaoFormatada.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            } else if (atleta.getPontuacao() < 0){
                pontuacaoFormatada.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color.cartoletaNegativa)), 0, pontuacaoFormatada.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            }

            switch (atleta.getPosicaoId()){
                case GOLEIRO:
                    loadAtletaImageWithPicasso( context, atleta.getFoto(), goleiro);
                    goleiroEscudo.setImageResource(ClubesUtil.getClubeEscudo(atleta.getClubeId()));
                    goleiroPontuacao.setText(pontuacaoFormatada);
                    goleiroVender.setVisibility(View.GONE);
                    break;
                case LATERAL:
                    if (qtdeZagueiros == 4 || qtdeZagueiros == 5){

                        if (lateralEsquerdoEmCampo == false){

                            loadAtletaImageWithPicasso(context, atleta.getFoto(), lateralEsquerdo);
                            lateralEsquerdoEscudo.setImageResource(ClubesUtil.getClubeEscudo(atleta.getClubeId()));
                            lateralEsquerdoPontuacao.setText(pontuacaoFormatada);
                            lateralEsquerdoVender.setVisibility(View.GONE);
                            lateralEsquerdoEmCampo = true;

                        } else if (lateralDireitoEmCampo == false){

                            loadAtletaImageWithPicasso(context, atleta.getFoto(), lateralDireito);
                            lateralDireitoEscudo.setImageResource(ClubesUtil.getClubeEscudo(atleta.getClubeId()));
                            lateralDireitoPontuacao.setText(pontuacaoFormatada);
                            lateralDireitoVender.setVisibility(View.GONE);
                            lateralDireitoEmCampo = true;
                        }
                    }
                    break;
                case ZAGUEIRO:
                    if (qtdeZagueiros == 3 || qtdeZagueiros == 5){

                        if (zagueiroEsquerdoEmCampo == false){

                            loadAtletaImageWithPicasso(context, atleta.getFoto(), zagueiroEsquerdo);
                            zagueiroEsquerdoEscudo.setImageResource(ClubesUtil.getClubeEscudo(atleta.getClubeId()));
                            zagueiroEsquerdoPontuacao.setText(pontuacaoFormatada);
                            zagueiroEsquerdoVender.setVisibility(View.GONE);
                            zagueiroEsquerdoEmCampo = true;

                        } else if (zaqueiroCentroEmCampo == false){

                            loadAtletaImageWithPicasso(context, atleta.getFoto(), zaqueiroCentro);
                            zaqueiroCentroEscudo.setImageResource(ClubesUtil.getClubeEscudo(atleta.getClubeId()));
                            zaqueiroCentroPontuacao.setText(pontuacaoFormatada);
                            zaqueiroCentroVender.setVisibility(View.GONE);
                            zaqueiroCentroEmCampo = true;

                        } else if (zagueiroDireitoEmCampo == false){

                            loadAtletaImageWithPicasso(context, atleta.getFoto(), zagueiroDireito);
                            zagueiroDireitoEscudo.setImageResource(ClubesUtil.getClubeEscudo(atleta.getClubeId()));
                            zagueiroDireitoPontuacao.setText(pontuacaoFormatada);
                            zagueiroDireitoVender.setVisibility(View.GONE);
                            zagueiroDireitoEmCampo = true;
                        }
                    } else if (qtdeZagueiros == 4){

                        if (zagueiroEsquerdoEmCampo == false){

                            loadAtletaImageWithPicasso(context, atleta.getFoto(), zagueiroEsquerdo);
                            zagueiroEsquerdoEscudo.setImageResource(ClubesUtil.getClubeEscudo(atleta.getClubeId()));
                            zagueiroEsquerdoPontuacao.setText(pontuacaoFormatada);
                            zagueiroEsquerdoVender.setVisibility(View.GONE);
                            zagueiroEsquerdoEmCampo = true;

                        } else if (zagueiroDireitoEmCampo == false){

                            loadAtletaImageWithPicasso(context, atleta.getFoto(), zagueiroDireito);
                            zagueiroDireitoEscudo.setImageResource(ClubesUtil.getClubeEscudo(atleta.getClubeId()));
                            zagueiroDireitoPontuacao.setText(pontuacaoFormatada);
                            zagueiroDireitoVender.setVisibility(View.GONE);
                            zagueiroDireitoEmCampo = true;
                        }
                    }
                    break;
                case MEIA:
                    if (qtdeMeias == 5){

                        if (pontaEsquerdoEmCampo == false){

                            loadAtletaImageWithPicasso(context, atleta.getFoto(), pontaEsquerdo);
                            pontaEsquerdoEscudo.setImageResource(ClubesUtil.getClubeEscudo(atleta.getClubeId()));
                            pontaEsquerdoPontuacao.setText(pontuacaoFormatada);
                            pontaEsquerdoVender.setVisibility(View.GONE);
                            pontaEsquerdoEmCampo = true;

                        } else if (meiaEsquerdoEmCampo == false){

                            loadAtletaImageWithPicasso(context, atleta.getFoto(), meiaEsquerdo);
                            meiaEsquerdoEscudo.setImageResource(ClubesUtil.getClubeEscudo(atleta.getClubeId()));
                            meiaEsquerdoPontuacao.setText(pontuacaoFormatada);
                            meiaEsquerdoVender.setVisibility(View.GONE);
                            meiaEsquerdoEmCampo = true;

                        } else if (volanteEmCampo == false){

                            loadAtletaImageWithPicasso(context, atleta.getFoto(), volante);
                            volanteEscudo.setImageResource(ClubesUtil.getClubeEscudo(atleta.getClubeId()));
                            volantePontuacao.setText(pontuacaoFormatada);
                            volanteVender.setVisibility(View.GONE);
                            volanteEmCampo = true;

                        } else if (meiaDireitoEmCampo == false){

                            loadAtletaImageWithPicasso(context, atleta.getFoto(), meiaDireito);
                            meiaDireitoEscudo.setImageResource(ClubesUtil.getClubeEscudo(atleta.getClubeId()));
                            meiaDireitoPontuacao.setText(pontuacaoFormatada);
                            meiaDireitoVender.setVisibility(View.GONE);
                            meiaDireitoEmCampo = true;

                        } else if (pontaDireitoEmCampo == false){

                            loadAtletaImageWithPicasso(context, atleta.getFoto(), pontaDireito);
                            pontaDireitoEscudo.setImageResource(ClubesUtil.getClubeEscudo(atleta.getClubeId()));
                            pontaDireitoPontuacao.setText(pontuacaoFormatada);
                            pontaDireitoVender.setVisibility(View.GONE);
                            pontaDireitoEmCampo = true;
                        }
                    } else if (qtdeMeias == 4) {

                        if (pontaEsquerdoEmCampo == false) {

                            loadAtletaImageWithPicasso(context, atleta.getFoto(), pontaEsquerdo);
                            pontaEsquerdoEscudo.setImageResource(ClubesUtil.getClubeEscudo(atleta.getClubeId()));
                            pontaEsquerdoPontuacao.setText(pontuacaoFormatada);
                            pontaEsquerdoVender.setVisibility(View.GONE);
                            pontaEsquerdoEmCampo = true;

                        } else if (meiaEsquerdoEmCampo == false) {

                            loadAtletaImageWithPicasso(context, atleta.getFoto(), meiaEsquerdo);
                            meiaEsquerdoEscudo.setImageResource(ClubesUtil.getClubeEscudo(atleta.getClubeId()));
                            meiaEsquerdoPontuacao.setText(pontuacaoFormatada);
                            meiaEsquerdoVender.setVisibility(View.GONE);
                            meiaEsquerdoEmCampo = true;

                        } else if (meiaDireitoEmCampo == false) {

                            loadAtletaImageWithPicasso(context, atleta.getFoto(), meiaDireito);
                            meiaDireitoEscudo.setImageResource(ClubesUtil.getClubeEscudo(atleta.getClubeId()));
                            meiaDireitoPontuacao.setText(pontuacaoFormatada);
                            meiaDireitoVender.setVisibility(View.GONE);
                            meiaDireitoEmCampo = true;

                        } else if (pontaDireitoEmCampo == false) {

                            loadAtletaImageWithPicasso(context, atleta.getFoto(), pontaDireito);
                            pontaDireitoEscudo.setImageResource(ClubesUtil.getClubeEscudo(atleta.getClubeId()));
                            pontaDireitoPontuacao.setText(pontuacaoFormatada);
                            pontaDireitoVender.setVisibility(View.GONE);
                            pontaDireitoEmCampo = true;
                        }
                    } else if (qtdeMeias == 3){

                        if (meiaEsquerdoEmCampo == false){

                            loadAtletaImageWithPicasso(context, atleta.getFoto(), meiaEsquerdo);
                            meiaEsquerdoEscudo.setImageResource(ClubesUtil.getClubeEscudo(atleta.getClubeId()));
                            meiaEsquerdoPontuacao.setText(pontuacaoFormatada);
                            meiaEsquerdoVender.setVisibility(View.GONE);
                            meiaEsquerdoEmCampo = true;

                        } else if (volanteEmCampo == false){

                            loadAtletaImageWithPicasso(context, atleta.getFoto(), volante);
                            volanteEscudo.setImageResource(ClubesUtil.getClubeEscudo(atleta.getClubeId()));
                            volantePontuacao.setText(pontuacaoFormatada);
                            volanteVender.setVisibility(View.GONE);
                            volanteEmCampo = true;

                        } else if (meiaDireitoEmCampo == false){

                            loadAtletaImageWithPicasso(context, atleta.getFoto(), meiaDireito);
                            meiaDireitoEscudo.setImageResource(ClubesUtil.getClubeEscudo(atleta.getClubeId()));
                            meiaDireitoPontuacao.setText(pontuacaoFormatada);
                            meiaDireitoVender.setVisibility(View.GONE);
                            meiaDireitoEmCampo = true;
                        }
                    }
                    break;
                case ATACANTE:
                    if (qtdeAtacantes == 3){

                        if (atacanteEsquerdaEmCampo == false){

                            loadAtletaImageWithPicasso(context, atleta.getFoto(), atacanteEsquerda);
                            atacanteEsquerdaEscudo.setImageResource(ClubesUtil.getClubeEscudo(atleta.getClubeId()));
                            atacanteEsquerdaPontuacao.setText(pontuacaoFormatada);
                            atacanteEsquerdaVender.setVisibility(View.GONE);
                            atacanteEsquerdaEmCampo = true;

                        } else if (atacanteCentroEmCampo == false){

                            loadAtletaImageWithPicasso( context, atleta.getFoto(), atacanteCentro);
                            atacanteCentroEscudo.setImageResource(ClubesUtil.getClubeEscudo(atleta.getClubeId()));
                            atacanteCentroPontuacao.setText(pontuacaoFormatada);
                            atacanteCentroVender.setVisibility(View.GONE);
                            atacanteCentroEmCampo = true;

                        } else if (atacanteDireitaEmCampo == false){

                            loadAtletaImageWithPicasso( context, atleta.getFoto(), atacanteDireita);
                            atacanteDireitaEscudo.setImageResource(ClubesUtil.getClubeEscudo(atleta.getClubeId()));
                            atacanteDireitaPontuacao.setText(pontuacaoFormatada);
                            atacanteDireitaVender.setVisibility(View.GONE);
                            atacanteDireitaEmCampo = true;
                        }
                    } else if (qtdeAtacantes == 2){

                        if (atacanteEsquerdaEmCampo == false){

                            loadAtletaImageWithPicasso(context, atleta.getFoto(), atacanteEsquerda);
                            atacanteEsquerdaEscudo.setImageResource(ClubesUtil.getClubeEscudo(atleta.getClubeId()));
                            atacanteEsquerdaPontuacao.setText(pontuacaoFormatada);
                            atacanteEsquerdaVender.setVisibility(View.GONE);
                            atacanteEsquerdaEmCampo = true;

                        } else if (atacanteDireitaEmCampo == false){

                            loadAtletaImageWithPicasso( context, atleta.getFoto(), atacanteDireita);
                            atacanteDireitaEscudo.setImageResource(ClubesUtil.getClubeEscudo(atleta.getClubeId()));
                            atacanteDireitaPontuacao.setText(pontuacaoFormatada);
                            atacanteDireitaVender.setVisibility(View.GONE);
                            atacanteDireitaEmCampo = true;
                        }
                    } else if (qtdeAtacantes == 1){

                        loadAtletaImageWithPicasso( context, atleta.getFoto(), atacanteCentro);
                        atacanteCentroEscudo.setImageResource(ClubesUtil.getClubeEscudo(atleta.getClubeId()));
                        atacanteCentroPontuacao.setText(pontuacaoFormatada);
                        atacanteCentroVender.setVisibility(View.GONE);
                        atacanteCentroEmCampo = true;
                    }
                    break;
                case TECNICO:
                    loadAtletaImageWithPicasso( context, atleta.getFoto(), tecnico);
                    tecnicoEscudo.setImageResource(ClubesUtil.getClubeEscudo(atleta.getClubeId()));
                    tecnicoPontuacao.setText(pontuacaoFormatada);
                    tecnicoVender.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        }
    }
}