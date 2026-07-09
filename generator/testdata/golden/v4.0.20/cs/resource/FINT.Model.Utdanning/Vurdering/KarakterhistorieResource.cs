using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

using FINT.Model.Felles.Kompleksedatatyper;

namespace FINT.Model.Utdanning.Vurdering
{

    public class KarakterhistorieResource 
    {

    
        public DateTime EndretDato { get; set; }
        public Identifikator SystemId { get; set; }
        
        public KarakterhistorieResource()
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
     
            

        public void AddOppdatertAv(Link link)
        {
            AddLink("oppdatertAv", link);
        }

        public void AddOpprinneligKarakterverdi(Link link)
        {
            AddLink("opprinneligKarakterverdi", link);
        }

        public void AddOpprinneligKarakterstatus(Link link)
        {
            AddLink("opprinneligKarakterstatus", link);
        }

        public void AddKarakterverdi(Link link)
        {
            AddLink("karakterverdi", link);
        }

        public void AddKarakterstatus(Link link)
        {
            AddLink("karakterstatus", link);
        }
    }
}
