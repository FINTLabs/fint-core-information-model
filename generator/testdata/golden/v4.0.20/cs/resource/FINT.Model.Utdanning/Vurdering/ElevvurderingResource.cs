using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Utdanning.Vurdering
{

    public class ElevvurderingResource 
    {

    
        public Identifikator SystemId { get; set; }
        
        public ElevvurderingResource()
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
     
            

        public void AddElevforhold(Link link)
        {
            AddLink("elevforhold", link);
        }

        public void AddSluttfagvurdering(Link link)
        {
            AddLink("sluttfagvurdering", link);
        }

        public void AddUnderveisordensvurdering(Link link)
        {
            AddLink("underveisordensvurdering", link);
        }

        public void AddVitnemalsmerknad(Link link)
        {
            AddLink("vitnemalsmerknad", link);
        }

        public void AddUnderveisfagvurdering(Link link)
        {
            AddLink("underveisfagvurdering", link);
        }

        public void AddHalvarsordensvurdering(Link link)
        {
            AddLink("halvarsordensvurdering", link);
        }

        public void AddHalvarsfagvurdering(Link link)
        {
            AddLink("halvarsfagvurdering", link);
        }

        public void AddSluttordensvurdering(Link link)
        {
            AddLink("sluttordensvurdering", link);
        }

        public void AddEksamensvurdering(Link link)
        {
            AddLink("eksamensvurdering", link);
        }
    }
}
