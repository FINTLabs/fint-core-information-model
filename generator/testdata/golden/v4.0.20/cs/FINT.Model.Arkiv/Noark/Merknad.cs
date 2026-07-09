using System;
using System.Collections.Generic;



namespace FINT.Model.Arkiv.Noark
{
	public class Merknad {
		public enum Relasjonsnavn
        {
			MERKNADSTYPE,
			MERKNADREGISTRERTAV
        }
        
	
		public DateTime Merknadsdato { get; set; }
		public string Merknadstekst { get; set; }
		
	}
}
