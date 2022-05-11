package scalable.com.shared.classes;

import scalable.com.shared.App;

public class Controller  {
App app;
public Controller(App app){
    this.app=app;
}
public void handleMessage(){
    
}
public void start(){
    ThreadPoolManager threadPool=this.app.getThreadPool();
    //TODO get  the thread count from properties
    threadPool.initThreadPool(ServiceConstants.DEFAULT_THREADS_COUNT);

}


}
