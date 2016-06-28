import java.util.List;  
import java.util.concurrent.Callable;  
import java.util.concurrent.CompletionService;  
import java.util.concurrent.ExecutionException;  
import java.util.concurrent.ExecutorCompletionService;  
import java.util.concurrent.ExecutorService;  
import java.util.concurrent.Executors;  
import java.util.concurrent.Future;  


/**
*CompletionService: 当executorService遇到BlockingQueue
* CompletionService整合了Executor和BlockingQueue的功能，你可以将Callable任务提交给它去执行，然后使用类似于队列中的take和poll方法，在结果完成可用时，获得这个结果，像一个打包的Future.
*
*/
public class FutureRenderer2 {  
    private static final int NTHREADS=100;  
    private static final ExecutorService exec=Executors.newFixedThreadPool(NTHREADS);  
      
    void renderPage(CharSequence source){  
        final List<ImageInfo> imageinfos=scanForImageInfo(source);  
        
        //CompletionService将executorService与BlockingQueue融合  
        CompletionService<ImageData> completionService=new ExecutorCompletionService<ImageData>(exec);  
          
        for(final ImageInfo imageinfo:imageinfos){  
            completionService.submit(new Callable<ImageData>(){  
                public ImageData call() throws Exception {  
                    //提高性能点一： 将顺序的下载，变成并发的下载，缩短下载时间  
                    return imageinfo.downloadImage();  
                }  
            });  
        }  
        renderText(source);  
        try {  
            for(int i=0;i<imageinfos.size();i++){  
                //CompletionService将executorService与BlockingQueue融合后,Future结果委托到BlockingQueue的take与poll方法
                Future<ImageData> f=completionService.take();  
                //提高性能点二： 下载完成一张图片后，立刻渲染到页面。  
                ImageData imagedata=f.get();  
                reanderImage(imagedata);  
            }  
        } catch (InterruptedException e) {  
            Thread.currentThread().interrupt();  
        }catch(ExecutionException e){  
            e.printStackTrace();  
              
        }  
    }  
  
    private void renderText(CharSequence source) {  
        // TODO Auto-generated method stub  
          
    }  
  
    private void reanderImage(ImageData data) {  
        // TODO Auto-generated method stub  
          
    }  
  
    private List<ImageInfo> scanForImageInfo(CharSequence source) {  
        // TODO Auto-generated method stub  
        return null;  
    }  
}  
