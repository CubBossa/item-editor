package de.bossascrew.itemeditor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Pair<L, R> {

	private L left;
	private R right;
}
