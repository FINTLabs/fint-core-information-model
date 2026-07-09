using System;
using System.Collections.Generic;



using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Felles.Kodeverk
{
	public class Valuta {
	
		public Identifikator Bokstavkode { get; set; }
		public string Navn { get; set; }
		public Identifikator Nummerkode { get; set; }
		
	}
}
