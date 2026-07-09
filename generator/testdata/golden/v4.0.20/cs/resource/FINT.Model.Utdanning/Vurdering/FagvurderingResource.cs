using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Utdanning.Vurdering
{

    public abstract class FagvurderingResource 
    {

    
        public string Kommentar { get; set; }
        public Identifikator SystemId { get; set; }
        public DateTime Vurderingsdato { get; set; }
        
        protected FagvurderingResource()
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
     
            

        public void AddFag(Link link)
        {
            AddLink("fag", link);
        }

        public void AddSkolear(Link link)
        {
            AddLink("skolear", link);
        }

        public void AddKarakter(Link link)
        {
            AddLink("karakter", link);
        }
    }
}
