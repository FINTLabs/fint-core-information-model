using System;
using System.Collections.Generic;



using FINT.Model.Utdanning.Basisklasser;

namespace FINT.Model.Utdanning.Elev
{
	public class Undervisningsforhold : Utdanningsforhold {
		public enum Relasjonsnavn
        {
			ARBEIDSFORHOLD,
			TIME,
			SKOLE,
			KLASSE,
			KONTAKTLARERGRUPPE,
			SKOLERESSURS,
			UNDERVISNINGSGRUPPE,
			EKSAMENSGRUPPE
        }
        
	
		public bool? Hovedskole { get; set; }
		
	}
}
