using System;
using System.Collections.Generic;
using Newtonsoft.Json;
using FINT.Model.Resource;

namespace FINT.Model.Arkiv.Noark
{

    public class DokumentbeskrivelseResource 
    {

    
        public string Beskrivelse { get; set; }
        public long? Dokumentnummer { get; set; }
        public List<DokumentobjektResource> Dokumentobjekt { get; set; }
        public List<string> Forfatter { get; set; }
        public DateTime? OpprettetDato { get; set; }
        public List<PartResource> Part { get; set; }
        public List<string> ReferanseArkivdel { get; set; }
        public SkjermingResource Skjerming { get; set; }
        public DateTime? TilknyttetDato { get; set; }
        public string Tittel { get; set; }
        
        public DokumentbeskrivelseResource()
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
     
            

        public void AddDokumentstatus(Link link)
        {
            AddLink("dokumentstatus", link);
        }

        public void AddDokumentType(Link link)
        {
            AddLink("dokumentType", link);
        }

        public void AddTilknyttetRegistreringSom(Link link)
        {
            AddLink("tilknyttetRegistreringSom", link);
        }

        public void AddTilknyttetAv(Link link)
        {
            AddLink("tilknyttetAv", link);
        }

        public void AddOpprettetAv(Link link)
        {
            AddLink("opprettetAv", link);
        }
    }
}
