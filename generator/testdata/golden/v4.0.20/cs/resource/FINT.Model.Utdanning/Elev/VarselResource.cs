using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Utdanning.Elev
{

    public class VarselResource 
    {

    
        public long Fravarsprosent { get; set; }
        public DateTime Sendt { get; set; }
        public Identifikator SystemId { get; set; }
        public string Tekst { get; set; }
        
        public VarselResource()
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
     
            

        public void AddUtsteder(Link link)
        {
            AddLink("utsteder", link);
        }

        public void AddKarakteransvarlig(Link link)
        {
            AddLink("karakteransvarlig", link);
        }

        public void AddType(Link link)
        {
            AddLink("type", link);
        }

        public void AddFaggruppemedlemskap(Link link)
        {
            AddLink("faggruppemedlemskap", link);
        }
    }
}
