using System;
using System.Collections.Generic;



using FINT.Model.Utdanning.Basisklasser;

namespace FINT.Model.Utdanning.Vurdering
{
	public class Eksamensgruppe : Gruppe {
		public enum Relasjonsnavn
        {
			UNDERVISNINGSFORHOLD,
			EKSAMEN,
			FAG,
			SKOLE,
			TERMIN,
			EKSAMENSFORM,
			SKOLEAR,
			GRUPPEMEDLEMSKAP,
			SENSOR
        }
        
	
		public DateTime? Eksamensdato { get; set; }
		
	}
}
