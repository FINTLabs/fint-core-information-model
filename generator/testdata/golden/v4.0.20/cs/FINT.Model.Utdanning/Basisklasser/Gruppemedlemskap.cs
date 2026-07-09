using System;
using System.Collections.Generic;



using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Utdanning.Basisklasser
{
	public abstract class Gruppemedlemskap {
	
		public Periode Gyldighetsperiode { get; set; }
		public Identifikator SystemId { get; set; }
		
	}
}
