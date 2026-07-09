using System;
using System.Collections.Generic;



using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Arkiv.Noark
{
	public class Dokumentfil {
	
		public string Data { get; set; }
		public string Filnavn { get; set; }
		public string Format { get; set; }
		public Identifikator SystemId { get; set; }
		
	}
}
