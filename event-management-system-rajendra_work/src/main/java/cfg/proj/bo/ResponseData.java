package cfg.proj.bo;

import java.util.List;

import cfg.proj.Entities.FeedbackEntity;
import cfg.proj.Entities.UserEntity;
import lombok.Data;

@Data
public class ResponseData {
    private String status;
    private String message;
    private Object data;
	public static ResponseData ok(FeedbackEntity saved) {
		// TODO Auto-generated method stub
		return null;
	}
	public static ResponseData error(String string) {
		// TODO Auto-generated method stub
		return null;
	}
	public static ResponseData ok(UserEntity user, String string) {
		// TODO Auto-generated method stub
		return null;
	}
	public static ResponseData ok(List<UserEntity> allUsers, String string) {
		// TODO Auto-generated method stub
		return null;
	}
}