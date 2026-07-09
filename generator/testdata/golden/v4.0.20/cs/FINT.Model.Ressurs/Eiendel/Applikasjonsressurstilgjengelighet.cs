using System;
using System.Collections.Generic;



using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Ressurs.Eiendel
{
	public class Applikasjonsressurstilgjengelighet {
		public enum Relasjonsnavn
        {
			KONSUMENT,
			RESSURS
        }
        
	
		public Periode Gyldighetsperiode { get; set; }
		public long? Lisensantall { get; set; }
		public Identifikator SystemId { get; set; }
		
	}
}
