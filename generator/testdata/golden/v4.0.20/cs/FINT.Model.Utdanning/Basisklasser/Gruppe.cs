using System;
using System.Collections.Generic;



using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Utdanning.Basisklasser
{
	public abstract class Gruppe {
	
		public string Beskrivelse { get; set; }
		public string Navn { get; set; }
		public Identifikator SystemId { get; set; }
		
	}
}
