using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Utdanning.Vurdering
{

    public abstract class OrdensvurderingResource 
    {

    
        public string Kommentar { get; set; }
        public Identifikator SystemId { get; set; }
        public DateTime Vurderingsdato { get; set; }
        
        protected OrdensvurderingResource()
        {
            Links = new Dictionary<string, List<Link>>();
        }

        [JsonProperty(PropertyName = "_links")]
        public Dictionary<string, List<Link>> Links { get; private set; }

        protected void AddLink(string key, Link link)
        {
            if (!Links.ContainsKey(key))
            {
                Links.Add(key, new List<Link>());
            }
            Links[key].Add(link);
        }
     
            

        public void AddAtferd(Link link)
        {
            AddLink("atferd", link);
        }

        public void AddOrden(Link link)
        {
            AddLink("orden", link);
        }

        public void AddSkolear(Link link)
        {
            AddLink("skolear", link);
        }
    }
}
