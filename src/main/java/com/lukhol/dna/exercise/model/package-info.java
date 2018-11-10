@org.hibernate.annotations.GenericGenerators({
	@org.hibernate.annotations.GenericGenerator(
		name = "myNative",
		strategy = "native"
	),
	@org.hibernate.annotations.GenericGenerator(
		name = "custom-uuid-generator",
		strategy = "guid"
	)
})

package com.lukhol.dna.exercise.model;