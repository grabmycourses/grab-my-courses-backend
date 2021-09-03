package open.seats.tracker.response;

import lombok.Data;

@Data
public class FaqResponse {
	private String question;
	private String answer;
	private boolean open=false;
	
	public FaqResponse(String qus, String ans) {
		this.question = qus;
		this.answer = ans;
	}
}
