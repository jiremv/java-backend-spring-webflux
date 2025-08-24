package software.amazonaws.example.solicitud.solicitud;

import org.crac.Context;
import org.crac.Core;
import org.crac.Resource;
import org.springframework.context.annotation.Configuration;
import software.amazonaws.example.solicitud.solicitud.dao.DynamoSolicitudDao;
@Configuration
public class PrimingResource implements Resource {

  private final DynamoSolicitudDao solicitudDao;
  public PrimingResource(DynamoSolicitudDao solicitudDao) {
    this.solicitudDao = solicitudDao;
    Core.getGlobalContext().register(this);
  }
  @Override
  public void beforeCheckpoint(Context<? extends Resource> context) throws Exception {
    System.out.println("beforeCheckpoint hook");
    //Below line would initialize the AWS SDK DynamoDBClient class. This technique is called "Priming".
    solicitudDao.describeTable();
  }
  @Override
  public void afterRestore(Context<? extends Resource> context) throws Exception {
    System.out.println("afterRestore hook");
  }
}