package cu.nat.wenisimo.appdomino.dominoapi;


import cu.nat.wenisimo.appdomino.models.BoletaPareja;
import cu.nat.wenisimo.appdomino.models.BoletaRespuesta;
import cu.nat.wenisimo.appdomino.models.DataRespuesta;
import cu.nat.wenisimo.appdomino.models.EventoRespuesta;
import cu.nat.wenisimo.appdomino.models.MesaRespuesta;
import cu.nat.wenisimo.appdomino.models.ParejaRespuesta;
import cu.nat.wenisimo.appdomino.models.ParejasMesaRespuesta;
import cu.nat.wenisimo.appdomino.models.Ronda;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface DominoApiService {
    @GET("api/eventos")
    Call<EventoRespuesta> obtenerEventos();

    @GET("api/eventos/iniciados")
    Call<EventoRespuesta> obtenerEventosIniciados();

    @FormUrlEncoded
    @POST("api/eventos/mesas")
    Call<MesaRespuesta> obtenerMesas(@Field("evento_id") int evento_id);

    @GET("api/evento/{evento_id}/mesa/{mesa_id}/parejas")
    Call<ParejasMesaRespuesta> obtenerParejasMesa(@Path("evento_id") int evento_id, @Path("mesa_id") int mesa_id);

    @GET("api/pareja/{id}")
    Call<ParejaRespuesta> obtenerParejas(@Path("id") int id);

    @GET("api/evento/{evento_id}/ronda/activa")
    Call<Ronda> obtenerRondaActiva(@Path("evento_id") int evento_id);

    @GET("api/evento/{evento_id}/mesa/{mesa_id}/boleta/ronda/activa")
    Call<BoletaRespuesta> obtenerBoleta(@Path("evento_id") int evento_id, @Path("mesa_id") int mesa_id);

    @GET("api/boletapareja/pareja/{boleta_pareja_id}")
    Call<BoletaPareja> salidor(@Path("boleta_pareja_id") int id);

    @FormUrlEncoded
    @POST("api/datas/create")
    Call<DataRespuesta> crearData(@Field("numero") int numero, @Field("boleta_id") int boleta_id, @Field("puntos") int puntos, @Field("duracion") int duracion, @Field("pareja_ganadora") int pareja_ganadora);
}