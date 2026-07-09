using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

using FINT.Model.Arkiv.Noark;

namespace FINT.Model.Arkiv.Samferdsel
{

    public class SoknadDrosjeloyveResource : SaksmappeResource 
    {

    
        public string Organisasjonsnavn { get; set; }
        public string Organisasjonsnummer { get; set; }
        
    }
}
