using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

using FINT.Model.Felles.Kompleksedatatyper;
using FINT.Model.Felles.Basisklasser;

namespace FINT.Model.Administrasjon.Organisasjon
{

    public class ArbeidslokasjonResource : EnhetResource 
    {

    
        public Identifikator Lokasjonskode { get; set; }
        public string Lokasjonsnavn { get; set; }
        
            

        public void AddArbeidsforhold(Link link)
        {
            AddLink("arbeidsforhold", link);
        }
    }
}
