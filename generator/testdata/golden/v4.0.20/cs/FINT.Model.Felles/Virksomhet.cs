using System;
using System.Collections.Generic;



using FINT.Model.Felles.Kompleksedatatyper;
using FINT.Model.Felles.Basisklasser;

namespace FINT.Model.Felles
{
	public class Virksomhet : Enhet {
		public enum Relasjonsnavn
        {
			LARLING
        }
        
	
		public Identifikator VirksomhetsId { get; set; }
		
	}
}
