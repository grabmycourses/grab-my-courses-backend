package open.seats.tracker.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="faqs")
public class Faq {

	@Id
	private int faqId;
	private String question;
	private String answer;
	private int displayOrder;
	private boolean active;
}
