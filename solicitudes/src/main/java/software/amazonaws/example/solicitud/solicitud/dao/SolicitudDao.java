package software.amazonaws.example.solicitud.solicitud.dao;

import software.amazonaws.example.solicitud.solicitud.entity.Solicitud;
import software.amazonaws.example.solicitud.solicitud.entity.Solicitudes;
import java.util.Optional;
public interface SolicitudDao {
  Optional<Solicitud> getSolicitud(String id);

  void putSolicitud(Solicitud solicitud);

  void deleteSolicitud(String id);

  Solicitudes getAllSolicitud();
}
