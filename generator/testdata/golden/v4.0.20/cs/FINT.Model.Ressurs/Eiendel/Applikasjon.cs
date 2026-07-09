using System;
using System.Collections.Generic;



using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Ressurs.Eiendel
{
	public class Applikasjon {
		public enum Relasjonsnavn
        {
			PLATTFORM,
			RESSURS,
			APPLIKASJONSKATEGORI
        }
        
	
		public string Beskrivelse { get; set; }
		public Periode Gyldighetsperiode { get; set; }
		public string Navn { get; set; }
		public Identifikator SystemId { get; set; }
		
	}
}
