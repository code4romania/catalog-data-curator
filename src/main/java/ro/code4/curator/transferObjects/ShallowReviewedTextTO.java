package ro.code4.curator.transferObjects;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ShallowReviewedTextTO {
	private int id;
	private String textType;
	private String textSourceId;
}
